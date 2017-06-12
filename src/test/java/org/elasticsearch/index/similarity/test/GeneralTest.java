/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elasticsearch.index.similarity.test;

import java.util.LinkedList;
import java.util.Map;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefBuilder;
import org.apache.lucene.util.LegacyNumericUtils;
import org.apache.lucene.util.NumericUtils;
import org.elasticsearch.common.settings.Settings;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import util.IntegerEncoding;

/**
 *
 * @author marijom
 */
public class GeneralTest {
    
    @Test
    public void testDecodeInteger(){
        int testNumber = 52341;
        //v2
        byte[] testBytes = {0x60, 0x8, 0x0, 0x3, 0x18, 0x75};
        BytesRef br = new BytesRef(testBytes);
        //v2
        int resultNumber = LegacyNumericUtils.prefixCodedToInt(br);
        assertEquals("Numbers are not the same", testNumber, resultNumber);
    }
    
    @Test
    public void testCodeInteger() {
        int shift = 0;
        int testNumber = 52341;
        byte[] testBytes = {0x60, 0x8, 0x0, 0x3, 0x18, 0x75};
        
        BytesRefBuilder brb = new BytesRefBuilder();
        LegacyNumericUtils.intToPrefixCoded(testNumber, shift, brb);
        BytesRef br = brb.toBytesRef();
        byte[] b = br.bytes;
        
        assertEquals("Byte array is not of the same length", testBytes.length, b.length);
        boolean different = false;
        for(int i=0; i<b.length; i++){
            if (b[i] != testBytes[i]){
                different = true;
            }
        }
        assertFalse("Array bytes are not the same!", different);       
    }
    
    @Test
    public void testSettings(){
        Settings settings = Settings.builder()
                .put("attributeWeights.streetNumberAlfa", 1)
                .put("attributeWeights.streetNumber", 2)
                .put("attributeWeights.streetName", 4)
                .put("attributeWeights.postalCode", 8)
                .put("attributeWeights.settlementName", 16)
                .put("attributeWeights.countyName", 32)
                .put("attributeTypes.streetNumberAlfa", "String")
                .put("attributeTypes.streetNumber", "String")
                .put("attributeTypes.streetName", "String")
                .put("attributeTypes.postalCode", "Integer")
                .put("attributeTypes.settlementName", "String")
                .put("attributeTypes.countyName", "String")
                .build();
        
        System.out.println("Settings: "+settings.toDelimitedString('#'));
        Map<String, Object> attributeWeights = settings.getByPrefix("attributeWeights.").getAsStructuredMap();
        System.out.println("Provider raw parameters: Weights: "+attributeWeights.toString());
        Map<String, String> attributeTypes = settings.getByPrefix("attributeTypes.").getAsMap();
        System.out.println("Provider raw parameters: Weights: "+attributeTypes.toString());        
    }
    
    @Test
    public void encodeDecodeTest() {
        byte[] testBytes = {0x35, 0x32, 0x33, 0x34, 0x31};
        int[] values = {52341};
        IntegerEncoding.IntegerCode coder = new IntegerEncoding.VLQ();
        
        for (int v: values) {
            byte[] code = coder.encode(v);
            int actual = coder.decode(code);
            System.out.println("expected: " + v + ", actual: " + actual + ", code: " + coder.toBinaryString(code));
            assert (coder.isValid(code));
            assert (v == actual);
        }

        for (int v: values) {
            String stringValue = String.valueOf(v);
            byte[] code = coder.fromDecimalString(stringValue);
            int actual = coder.decode(code);
            System.out.println("expected: " + v + ", actual: " + actual + ", code: " + coder.toBinaryString(code));
            assert (coder.isValid(code));
            assert (v == actual);
        }
    }

    private static void encodeDecodeTest(String[] values, IntegerEncoding.IntegerCode coder) {
        for (String v: values) {
            byte[] code = coder.fromBinaryString(v);
            String actual = coder.toBinaryString(code);
            assert (actual.equals(v));
        }
    }    
}
