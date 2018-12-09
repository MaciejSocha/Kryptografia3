package algorithm;

import java.io.File;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DSA implements Algorithm {
    private BigInteger q;
    private BigInteger p;
    private BigInteger k;
    private BigInteger g;
    private BigInteger x;
    private BigInteger h;
    private BigInteger y;
    private File file;

    @Override
    public boolean verifyFile(String publicKey, File file) {
        this.file = file;
        return calculateR(g, k, p, q).equals(calculateV(g, k, y, p, x, q));
    }

    private BigInteger calculateR(BigInteger g, BigInteger k, BigInteger p, BigInteger q) {
        BigInteger ret = g.modPow(k, p);
        return ret.mod(q);
    }

    private BigInteger calculateV(BigInteger g, BigInteger k, BigInteger y, BigInteger p, BigInteger x, BigInteger q) {
        BigInteger r = calculateR(g, k, p, q);
        BigInteger w = calculateW(g, k, p, x, q);
        BigInteger ret = (g.modPow(calculateU2(r, w, q), p).multiply(y.modPow(calculateU2(r, w, q), p))).mod(p);
        ret = ret.mod(q);
        return ret;
    }

    private BigInteger calculateW(BigInteger g, BigInteger k, BigInteger p, BigInteger x, BigInteger q) {
        BigInteger s = calculateS(g, k, p, x, q);
        BigInteger minusOne = new BigInteger("-1");
        return s.modPow(minusOne, q);
    }

    private BigInteger calculateS(BigInteger g, BigInteger k, BigInteger p, BigInteger x, BigInteger q) {
        BigInteger r = calculateR(g, k, p, q);
        return k.pow(-1).multiply(calculateH().add(x.multiply(r)));
    }

    private BigInteger calculateH() {
        return new BigInteger("0");//TODO should take file
    }

    private BigInteger calculateU2(BigInteger r, BigInteger w, BigInteger q) {
        return ((r.mod(q)).multiply(w.mod(q))).mod(q);
    }

    @Override
    public String generateKey(String privateKey, File file) {
        return null;
    }

    public void generateNumbers() {
        Random r = new Random();
        q = BigInteger.probablePrime(160, new Random());

        int l = -10;
        while (l % 64 != 0)
            l = ThreadLocalRandom.current().nextInt(512, 1024);
        p = BigInteger.probablePrime(l, new Random());

        k = new BigInteger(r.nextInt(159), new Random());

        x = new BigInteger(r.nextInt(159), new Random());

        h = new BigInteger(r.nextInt(l - 1), new Random());

        g = (h.modPow((p.subtract(BigInteger.ONE)).divide(q), p));

        y = g.modPow(x, p);
    }
}
