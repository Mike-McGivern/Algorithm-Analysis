import java.math.BigInteger;

public class ModularExponentiation {
   public static BigInteger modExp(BigInteger base, BigInteger exponent, BigInteger modulus) {
      if(modulus.equals(BigInteger.ONE)) 
         return BigInteger.ZERO;
      
      BigInteger result = BigInteger.ONE;
      base = base.mod(modulus);
      
      while(exponent.compareTo(BigInteger.ZERO) > 0) {
         if(exponent.testBit(0)) {
            result = result.multiply(base).mod(modulus);
         }
         base = base.multiply(base).mod(modulus);
         exponent = exponent.shiftRight(1);
      }
   
      return result;
   }
}