import java.math.BigInteger;

public class ExtendedEuclid {
   public static class Result {
      public BigInteger gcd, x, y;
      
      public Result(BigInteger gcd, BigInteger x, BigInteger y) {
         this.gcd = gcd;
         this.x = x;
         this.y = y;
      }
      
   }
   
   public static Result extendedGCD(BigInteger a, BigInteger b) {
      if(b.equals(BigInteger.ZERO)) {
         return new Result(a, BigInteger.ONE, BigInteger.ZERO);
      }
      
      Result next = extendedGCD(b, a.mod(b));
      BigInteger x = next.y;
      BigInteger y = next.x.subtract(a.divide(b).multiply(next.y));
      return new Result(next.gcd, x, y);
   }
   
   public static BigInteger modInverse(BigInteger a, BigInteger m) {
      Result result = extendedGCD(a, m);
      if(!result.gcd.equals(BigInteger.ONE)) {
         throw new ArithmeticException("Inverse DNE");
         
      }
      return result.x.mod(m);
   }
}