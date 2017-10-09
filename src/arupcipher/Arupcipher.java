/*Luis F Rivera Jr

 */
package arupcipher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


/**
 *
 * @author Luis
 */
public class Arupcipher {
    private static final int[] primeNumbersArray = new int[2000000];
    private static final String base64Characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    
    /*  
        This calculates the prime numbers up to numberOfPrimes using the Sieve of Eranthoses
        The prime numbers are indicated by making the attribute false at the index 
        number of the prime
        The sequence of prime numbers is then placed into an array for indexing
    */
    public static void calculateSOE(){
        int i, j, k, counter1 = 0;
        int numberOfPrimes = 2000000;
        boolean primeNumberArray []= new boolean[numberOfPrimes];
        
        //Initialize boolean primeNumberArray
        for(i = 2; i < numberOfPrimes; i++){
            primeNumberArray[i] = true;            
        }
        
        for(i = 2; i < numberOfPrimes; i++){
            if(primeNumberArray[i]){
               for(j = 0; j < numberOfPrimes; j++){
                   k = (i * 2) + (j * i); 
                   if(k >= numberOfPrimes)
                        break;
                   primeNumberArray[k] = false;

               }
            }
        }
        //Put prime numbers into array
        for(i = 0; i < numberOfPrimes; i ++){
            if(primeNumberArray[i] == true){
                primeNumbersArray[counter1] = i;
                counter1++;
            }
        }
        System.out.println("Primes are: " + counter1);
        for(i = 0; i < primeNumbersArray.length; i ++){
            //System.out.println(primeNumbersArray[i]);
            if(primeNumbersArray[i] == 0)
                break;
        }
        System.out.println();
   
    }//End of function calculateSOE
    /*
        This method is used to encrypt the plaintext with the two keys provided
        from the file input.
        This method outputs an encrypted string as the ciphertext    
    */
    
    public static String encryptPlaintext(char keyOne [][], int keyPrime, char plaintext[]){
        int i , j, k, charFound = 0, counter = 0; 
        String tempCipherText = "";
        int[] iCipherText = new int [plaintext.length * 2]; //Intermediate CipherText
        
        //Start loop to encrypt each letter
        //Rotate the box after encrypting each letter
        for(i = 0; i < plaintext.length; i++){
            
            //Traverse keyOne and find a matching letter
            //J is the row, K is the column
            //System.out.println("Lookin for " + plaintext[i]);
             for(j = 0; j < 8; j++){
                 if(charFound == 1)
                     break;
                for(k = 0; k < 8; k++){
                    if(plaintext[i] == keyOne[j][k]){
                        iCipherText[counter] = j;
                        iCipherText[counter + 1] = k;
                        counter = counter + 2;
                        charFound = 1;
                        break;
                    }
                }
            }
            
            //Reset charFound flag to 0 
            charFound = 0; 
            
            //Rotate the box
            keyOne = rotateBox(keyOne, i);
            /*System.out.println("new box is");
            for(j = 0; j < 8; j++){
                for(k = 0; k < 8; k++){
                    System.out.print(keyOne[j][k]);
                }
                System.out.println();
            }
            System.out.println();*/
        }

        //Use keyPrime to generate a sequence of prime numbers 
        int sequence [] = new int [plaintext.length * 2];
        sequence = generatePrimeSequence(keyPrime, plaintext.length);
        
        //Calculate base 8 digits of the ciphertext
        int [] baseEightCipher = new int [plaintext.length * 2];
        for(i = 0; i < plaintext.length * 2; i ++){
            baseEightCipher[i] = (iCipherText[i] + sequence[i]) % 8;
            //System.out.println(i + "  " + iCipherText[i] + " + " + sequence[i] + " %8 = " + baseEightCipher[i]);
        }
        
        //Change baseEightCipher to base 10
        int [] baseTenCipher = new int [plaintext.length];
        for(i = 0; i < plaintext.length; i++){
            baseTenCipher[i] = ( (8 * baseEightCipher[(2 * i)]) + baseEightCipher[(2 * i) + 1] );
            System.out.println(i + "  " + (8 * baseEightCipher[(2 * i)]) + " + " + baseEightCipher[(2 * i) + 1] + " = " + baseTenCipher[i]);
        }
        
        //Change baseTenCipher to radix-64 characters
        char radixCharacter;
        
        for(i = 0; i < baseTenCipher.length; i++){
            radixCharacter = base64Characters.charAt(baseTenCipher[i]);
            tempCipherText = tempCipherText + radixCharacter;
        }
        
         return tempCipherText;
    }//End of method encryptPlaintext
    
    /*
        This method is used to cyclically rotate the key matrix.
        If the number is even the box will rotate the row (i%16)/2 to the 
        right by one position.
        If the number is odd the box will rotate the column (i%16)/2 down 
        by one position.
    */
    
    public static char[][] rotateBox(char keyOne[][], int rotateInteger){
        char tempChar [] = new char [8];
        int i, j;
        
        if(rotateInteger%2 == 0){
            //Copy row (rotateInteger%16)/2) to tempChar
            for(i = 0; i < 8; i ++){
                tempChar[i] = keyOne[(rotateInteger%16)/2][i];
            }
            for(i = 0; i < 8; i ++){
                keyOne[(rotateInteger%16)/2][(i+1)%8] = tempChar[i];
            }
                        
        } else {
        //Copy column (rotateInteger%16)/2) to tempChar
            for(i = 0; i < 8; i ++){
                tempChar[i] = keyOne[i][(rotateInteger%16)/2];
            }
            for(i = 0; i < 8; i ++){
                keyOne[(i+1)%8][(rotateInteger%16)/2] = tempChar[i];
            }
        }   

        return keyOne;
    }//End of method rotateBox
    
    /*
        This method is used to generate a sequence of numbers from the prime numbers
    */
    public static int[] generatePrimeSequence(int keyPrime, int plaintextLength){
        int [] tmpPrimeSequence = new int [(plaintextLength * 2) + 1];
        int i, counter = 0;
        
        //Iterate through primeNumbersArray to find the startingpoint
        int startingPNumberLocation = 0;
        for(i = 0; i < primeNumbersArray.length; i++){
            //System.out.println("PrimeNumbers Array " + primeNumbersArray[j] + "  keyPrime " + keyPrime);
            if(primeNumbersArray[i] > keyPrime){
                startingPNumberLocation = i;
                break;
            }
            else{
                startingPNumberLocation = i;
            }
                
        }
        System.out.println("starting prime location is " + startingPNumberLocation + " number is " 
                            + primeNumbersArray[startingPNumberLocation]);
        System.out.println("number of characters is " + ((plaintextLength * 2)) );
        
        //Create a sequence of numbers by the subtracting the higher prime number from its predecessor
        //And dividing that by 2
        for (i = startingPNumberLocation;i < startingPNumberLocation + plaintextLength * 2; i++){
            
            tmpPrimeSequence[counter] = (primeNumbersArray[i+1] - primeNumbersArray[i]) / 2;
            //System.out.println(primeNumbersArray[i+1] + " - " + primeNumbersArray[i] + " = " + tmpPrimeSequence[counter] );
            counter ++;
        }
                        
        return tmpPrimeSequence;
    }//End of method generatePrimeSequence
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        int i;
        // Calculate the prime numbers and place into an array
        calculateSOE();
        
        // Read in data from file
        Scanner stdInput = new Scanner(System.in);
        
        char[][] keyMatrix = new char[8][8];
        String tmpInputString = new String();
        int j, numOfEncrypt = 0;

        //Input the name of the file to be accessed
        //System.out.println("Enter the name of the file for input");
        //String inputFileName = stdInput.next();
        String inputFileName = "testArup.txt";
        Scanner inputFile = new Scanner(new File(inputFileName));

        //Input the name of the file to be written to and open that file
        //System.out.println("What is the name of the file for output");
        //String outputFileName = stdInput.next();
        String outputFileName = "output.txt";
        BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputFileName));
        
        //Get first number from file as an int
        try{        
            tmpInputString = inputFile.nextLine();
            }
        catch(IllegalStateException e){
            System.err.println("File is closed " + e);
        }

        //Make first number an integer from a string
        numOfEncrypt = Integer.parseInt(tmpInputString);

        //System.out.println("First number in file = " + numOfEncrypt);

        //Start outer loop for number of encryptions
        for(int outerloop = 0; outerloop < numOfEncrypt; outerloop++){

            //Read in 8x8 matrix for the input key
            for(i = 0; i < 8; i++){
                tmpInputString = inputFile.next();

                for(j = 0; j < 8; j++){
                   
                   keyMatrix[i][j] = tmpInputString.charAt(j);
                   //System.out.print(keyMatrix[i][j]);
                }
                //System.out.println();
            }

            // Read in prime number from file for key 2
            int keyPrime;
            tmpInputString = inputFile.next();
            keyPrime = Integer.parseInt(tmpInputString);
            System.out.println("prime is " + keyPrime);
            
            //Read in plaintext from file
            tmpInputString = inputFile.next();
            char plaintext[] = new char[tmpInputString.length()];
            plaintext = tmpInputString.toCharArray();
            for(i = 0; i < tmpInputString.length(); i++)
                System.out.print(plaintext[i]);
            System.out.println();
            
            // Encrypt the plaintext and return a string with the encrypted plaintext
            String cipherText = "";
            cipherText = encryptPlaintext(keyMatrix, keyPrime, plaintext);

            //Write cipherText to file
            System.out.println("Cipher is " + cipherText);
            for(i = 0; i < cipherText.length(); i ++){
                outputFile.write(cipherText.charAt(i));
            }
            
            outputFile.newLine();
            
        }//End of outer loop
        
        //Close the input and output files
        inputFile.close();
        outputFile.close();
        
    }//End of Main
    
}//End of Class arupcipher