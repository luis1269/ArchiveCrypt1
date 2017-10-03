/*Luis F Rivera Jr

 */
package arupcipher;

/**
 *
 * @author Luis
 */
public class Arupcipher {

    /*  
        This calculates the prime numbers up to numberOfPrimes using the Sieve of Eranthoses
        The prime numbers are indicated by making the attribute false at the index 
        number of the prime
    */
    public static boolean [] calculateSOE(){
        
        int i, j, k;
        int numberOfPrimes = 1000000;
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
    
        return primeNumberArray;
    }//End of function calculateSOE
    public static void main(String[] args) {
        boolean [] primeNumberArray;
        int i;
       
        primeNumberArray = calculateSOE();
        
        for(i=2; i < primeNumberArray.length; i++){
            if(primeNumberArray[i]){
               System.out.println("prime " + i); 
            }
        }
    }//End of Main
    
}//End of Class arupcipher
