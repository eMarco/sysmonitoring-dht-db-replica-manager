/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unict.ing.pds.light.utils;

import org.unict.ing.pds.dhtdb.utils.dht.Key;
import java.util.BitSet;

/**
 *
 * @author Marco Grassia <marco.grassia@studium.unict.it>
 */
public class Label {
    private final String label;

    public Label(String label) {
        this.label = label;
    }

    public Label(byte[] label) {
        this.label = new String(label);
    }

    public String getLabel() {
        return "#" + this.label;
    }

    public Label(long timestamp) {
        // TODO

        this.label = String.valueOf(timestamp);
    }

    public Label prefix(int lenght) {
        long value = 0;

        BitSet labelBits = new BitSet(lenght);

        long lower, upper, mid;

        lower = Range.REPRESENTABLE_RANGE.getLower();
        upper = Range.REPRESENTABLE_RANGE.getUpper();

        for (int i = 0; i < lenght; i++) {
            mid = (upper - lower) / 2;

            if (value < mid) {
                labelBits.clear(i);

                upper = mid;
            }
            else {
                labelBits.set(i);

                lower = mid;
            }

        }

        return new Label(labelBits.toByteArray());
    }

    public int getLength() {
        return 0;
    }    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return this.getLabel();
    }

    public Label toDHTKey() {
        // TODO add #??
        return Label.namingFunction(this, 1);
    }

    public Key toKey() {
        return new Key(toDHTKey().getLabel(), true);
    }

    public Key toDataKey() {
        return new Key(toDHTKey().getLabel() + "DATA", true);
    }

    public Label namingFunction() {
        return Label.namingFunction(this, 1);
    }

    /**
     *
     * @param treeLenght
     * @param prefixLenght
     * @return
     */
    public Label nextNamingFunction(int treeLenght, int prefixLenght) {
        return Label.nextNamingFunction(this, prefixLenght, treeLenght);
    }

    /**
     *
     * @param label
     * @return
     */
    public static Label namingFunction(Label label) {
        return Label.namingFunction(label, 1);
    }

    /**
     *
     * @param label
     * @param dimentions
     * @return
     */
    public static Label namingFunction(Label label, int dimentions) {
        byte[] bytes = label.getBytes();

        BitSet bits = BitSet.valueOf(bytes);
        return namingFunction(bits, dimentions, bits.length());
    }

    public static Label namingFunction(BitSet bits, int dimentions, int len) {
        if (bits.get(len - dimentions) == bits.get(len)) {
            // Unset last bit
            bits.clear(len);

            return namingFunction(bits, dimentions, len - 1);
        } else {
            return new Label(bits.toByteArray());
        }
    }

    /**
     * Γ(μ) is the set of possible prefixes of μ
     * Γ(μ, D) set of possibile prefixes with maximum length D
     *
     * Notation: μ = label, x = prefix
     *
     * Locates the first bit in the suffix of μ (with respect to x) that differs from x’s ending bit; the value
     * nextNamingFunction(μ, x) is then the prefix of μ, which ends up with this located bit.
     *
     * fnn(x, μ) =
     *              p00∗1 ∈ Γ(μ) if x = p0,
     *              p11∗0 ∈ Γ(μ) if x = p1.
     *
     * Intuitively, fnn locates the first bit in the suffix of μ (with respect to x) that differs from x’s ending bit;
     * the value nextNamingFunction(μ, x) is then the prefix of μ, which ends up with this located bit.
     * @param label
     * @param prefixLength
     * @param treeLenght
     * @return
     */
    public static Label nextNamingFunction(Label label, int prefixLength, int treeLenght) {
        BitSet labelBits = label.getBitSet();

        int firstDifferentBit;

        // If prefix's last bit is 0 ==> p00∗1 (look for the first 1 bit)
        if (labelBits.get(prefixLength) == false) {
            firstDifferentBit = labelBits.nextSetBit(prefixLength);
        }
        // If prefix's last bit is 1 ==> p11∗0 (look for the first 0 bit)
        else {
            firstDifferentBit = labelBits.nextClearBit(prefixLength);
        }

        if (firstDifferentBit == -1) return null;

        return new Label(labelBits.get(0, firstDifferentBit).toByteArray());
    }

    /**
     *
     * @param labels
     * @return
     */
    public static Label lowestCommonAncestor(Label... labels) {
        if (labels.length < 2) return labels[0];

        BitSet prefix = lowestCommonAncestor(labels[0].getBitSet(), labels[1].getBitSet());

        // TODO optimize this loop!
        for (int i = 3; i < labels.length; i++) {
            prefix = lowestCommonAncestor(prefix, labels[i].getBitSet());
        }

        return new Label(prefix.toByteArray());
    }

    /**
     *
     * @param label1
     * @param label2
     * @return
     */
    public static BitSet lowestCommonAncestor(BitSet label1, BitSet label2) {
        BitSet xor = (BitSet) label1.clone();

        // Label1 XOR label2
        // xor[i] = 1 <==> label1[i] != label2[i]
        xor.xor(label2);

        // Return the prefix of one of the two parameter labels
        // Length of the prefix: Min(label1.len, label2.len, last_common_bit.pos)
        return label1.get(0, Integer.min(Integer.min(label1.length(), label2.length()), xor.nextSetBit(0)-1));
    }

    public static Range interval() {
        return interval(Range.REPRESENTABLE_RANGE);
    }

    // TODO : rename maximum_range!!
    public static Range interval(Range maximum_range) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isRight(){
        return true;
    }
    public boolean isLeft(){
        return false;
    }

    public Label childToLeft() {
        return new Label(this.label + "0");
    }

    public Label childToRight(){
        return new Label(this.label + "1");
    }

    private byte[] getBytes() {
        return this.label.getBytes();
    }

    private BitSet getBitSet() {
        return BitSet.valueOf(this.label.getBytes());
    }
}
