import java.math.BigInteger;
import java.security.SecureRandom;

public class MillerRabin {
   private static final SecureRandom random = new SecureRandom();
   
   public static boolean isProbablePrime(BigInteger n, int k) {
      if(n.equals(BigInteger.TWO) || n.equals(BigInteger.valueOf(3))) 
         return true;
      if(n.compareTo(BigInteger.TWO) < 0 || n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) 
         return false;
      
      BigInteger d = n.subtract(BigInteger.ONE);
      int r = 0;
      while(d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
         d = d.divide(BigInteger.TWO);
         r++;
      }
      
      for(int i = 0; i < k; i++) {
         BigInteger a = uniformRandom(BigInteger.TWO, n.subtract(BigInteger.TWO));
         BigInteger x = a.modPow(d, n);
         
         if(x.equals(BigInteger.ONE) || x.equals(n.subtract(n.subtract(BigInteger.ONE)))) 
            continue;
         
         boolean continueOuter = false;
         for(int j = 0; j < r - 1; j++) {
            x = x.modPow(BigInteger.TWO, n);
            if(x.equals(n.subtract(BigInteger.ONE))) {
               continueOuter = true;
               break;
            }
         }
         
         if(!continueOuter) 
            return false;
      }
      
      return true;
   }
   
   private static BigInteger uniformRandom(BigInteger min, BigInteger max) {
      BigInteger result;
      do {
         result = new BigInteger(max.bitLength(), random);
      } while(result.compareTo(min) < 0 || result.compareTo(max) > 0);
      return result;
   }
}

