import java.math.BigInteger;
import java.security.SecureRandom;
import java.io.*;

public class Assignment3 {
   private static BigInteger x, y, p, N, phi, s;

   public static void main(String[] args) {
      makeKeys();
      System.out.println("Keys Generated");
      System.out.println("p: \n" + p + "\ns: \n" + s + "\nN: \n" + N + "\nphi: \n" + phi);

      encrypt();
      decrypt();
   }

   public static void makeKeys() {
      x = BigInteger.probablePrime(2048, new SecureRandom());
      y = BigInteger.probablePrime(2048, new SecureRandom());
      while (x.equals(y)) {
         y = BigInteger.probablePrime(2048, new SecureRandom());
      }

      N = x.multiply(y);
      phi = x.subtract(BigInteger.ONE).multiply(y.subtract(BigInteger.ONE));

      SecureRandom rand = new SecureRandom();
      do {
         p = BigInteger.probablePrime(512, rand);
      } while (!phi.gcd(p).equals(BigInteger.ONE));

      s = p.modInverse(phi);

      try (ObjectOutputStream pubOut = new ObjectOutputStream(new FileOutputStream("pubKey.dat"))) {
         pubOut.writeObject(p);
         pubOut.writeObject(N);
      } catch (IOException e) {
         System.out.println("Error writing public key");
         System.exit(1);
      }

      try (ObjectOutputStream privOut = new ObjectOutputStream(new FileOutputStream("privKey.dat"))) {
         privOut.writeObject(s);
         privOut.writeObject(N);
      } catch (IOException e) {
         System.out.println("Error writing private key");
         System.exit(1);
      }
   }

   public static void encrypt() {
      try (ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream("pubKey.dat"));
           BufferedInputStream reader = new BufferedInputStream(new FileInputStream("Test.txt"));
           ObjectOutputStream pubOut = new ObjectOutputStream(new FileOutputStream("EncryptedTest.dat"))) {

         BigInteger p = (BigInteger) keyIn.readObject();
         BigInteger N = (BigInteger) keyIn.readObject();

         byte[] block = new byte[32];
         int bytesRead;

         while ((bytesRead = reader.read(block)) != -1) {
            if (bytesRead < 32) {
               for (int i = bytesRead; i < 32; i++) {
                  block[i] = 32; // pad with ASCII space
               }
            }

            BigInteger M = new BigInteger(1, block);
            BigInteger C = M.modPow(p, N);
            pubOut.writeObject(C);
         }

      } catch (FileNotFoundException e) {
         System.out.println("File Not Found: Test.txt or pubKey.dat");
         System.exit(1);
      } catch (IOException | ClassNotFoundException e) {
         System.out.println("Encryption error: " + e.getMessage());
         System.exit(1);
      }

      System.out.println("Encrypted file is created");
   }

   public static void decrypt() {
      try (ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream("privKey.dat"));
           ObjectInputStream cipherIn = new ObjectInputStream(new FileInputStream("EncryptedTest.dat"));
           BufferedOutputStream messageOut = new BufferedOutputStream(new FileOutputStream("Decrypted.txt"))) {

         BigInteger s = (BigInteger) keyIn.readObject();
         BigInteger N = (BigInteger) keyIn.readObject();

         while (true) {
            try {
               BigInteger C = (BigInteger) cipherIn.readObject();
               BigInteger M = C.modPow(s, N);

               byte[] block = M.toByteArray();
               byte[] paddedBlock = new byte[32];

               int start = block.length > 32 ? block.length - 32 : 0;
               int copyLen = Math.min(32, block.length);
               System.arraycopy(block, start, paddedBlock, 32 - copyLen, copyLen);

               messageOut.write(paddedBlock);
            } catch (EOFException eof) {
               break;
            }
         }

      } catch (FileNotFoundException e) {
         System.out.println("Private key or encrypted file not found.");
         System.exit(1);
      } catch (IOException | ClassNotFoundException e) {
         System.out.println("Decryption error: " + e.getMessage());
         System.exit(1);
      }

      System.out.println("Decrypted file is created, compare it to the original file");
   }
}