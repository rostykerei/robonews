package nl.rostykerei.news.service.image.tools;

import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.BitSet;

public class ImageHash {

    private static ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

    public static byte[] calculatePHash(BufferedImage source) {
        BufferedImage img = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.drawImage(source, 0, 0, 8, 8, null);
        g.dispose();

        colorConvert.filter(img, img);

        int total = 0;

        int[] map = new int[64];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                total += map[(x*8+y)] = (img.getRGB(x, y) & 0xff);
            }
        }

        int avg = total / 64;

        BitSet hash = new BitSet(64);

        for (int x = 0; x < 64; x++) {
            hash.set(x, map[x] > avg);
        }

        return hash.toByteArray();
    }

    // TODO redo for more efficient
    public static int hammingDistance(byte[] set1, byte[] set2) {
        if (set1 == null || set2 == null || set1.length == 0 || set1.length != set2.length) {
            throw new IllegalArgumentException();
        }

        BitSet bitSet1 = BitSet.valueOf(set1);
        BitSet bitSet2 = BitSet.valueOf(set2);

        int dist = 0;

        for (int i = 0; i < bitSet1.length(); i++) {
            if (bitSet1.get(i) != bitSet2.get(i)) {
                dist++;
            }
        }

        return dist;
    }

}
