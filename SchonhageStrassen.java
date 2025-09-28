import java.math.BigInteger;

public class SchonhageStrassen {
   public static BigInteger multiply(BigInteger a, BigInteger b) {
      int n = Math.max(a.bitLength(), b.bitLength());
      
      if(n < 1024) 
         return a.multiply(b);
      
      int k = 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(n));
      BigInteger mod = BigInteger.ONE.shiftLeft(k).add(BigInteger.ONE);
      
      BigInteger aMod = a.mod(mod);
      BigInteger bMod = b.mod(mod);
      
      BigInteger result = aMod.multiply(bMod).mod(mod);
      return result;
   }
}