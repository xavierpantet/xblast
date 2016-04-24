package ch.epfl.xblast;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class RunLengthEncoderTest {

    @Test
    public void encodeWorksOnConstantArray() {
        List<Byte> l = new LinkedList<>(Arrays.asList((byte) 3, (byte) 3, (byte) 3, (byte) 3));
        List<Byte> t = new LinkedList<>(Arrays.asList((byte) -2, (byte) 3));
        assertEquals(t, RunLengthEncoder.encode(l));
    }
    
    @Test
    public void encodeWorksOnConstantPlusElementArray(){
        List<Byte> l1 = new LinkedList<>(Arrays.asList((byte) 3, (byte) 3, (byte) 3, (byte) 3 ,(byte) 4));
        List<Byte> t1 = new LinkedList<>(Arrays.asList((byte) -2, (byte) 3, (byte) 4));
        assertEquals(t1, RunLengthEncoder.encode(l1));
    }
    
    @Test
    public void encodeWorksOnDoubleDoubleArray(){
        List<Byte> l2 = new LinkedList<>(Arrays.asList((byte) 3, (byte) 3, (byte) 4, (byte) 4));
        assertEquals(l2, RunLengthEncoder.encode(l2));
    }
    
    @Test
    public void encodeWorksOnSimpleDoubleArray(){
        List<Byte> l2 = new LinkedList<>(Arrays.asList((byte) 4, (byte) 3, (byte) 3));
        assertEquals(l2, RunLengthEncoder.encode(l2));
    }
    
    @Test
    public void encodeWorksOnSimpleSimpleArray(){
        List<Byte> l2 = new LinkedList<>(Arrays.asList((byte) 4, (byte) 3));
        assertEquals(l2, RunLengthEncoder.encode(l2));
    }
    
    @Test
    public void encodeWorksOnDoubleSimpleDoubleArray(){
        List<Byte> l2 = new LinkedList<>(Arrays.asList((byte) 3, (byte) 3, (byte) 4, (byte) 5, (byte) 5));
        assertEquals(l2, RunLengthEncoder.encode(l2));
    }
    
    @Test
    public void encodeWorksOnSimpleDoubleSimpleArray(){
        List<Byte> l2 = new LinkedList<>(Arrays.asList((byte) 5, (byte) 3, (byte) 3, (byte) 4));
        assertEquals(l2, RunLengthEncoder.encode(l2));
    }
    
    @Test
    public void encodeWorksOnDoubleSimpleArray(){
        List<Byte> l3 = new LinkedList<>(Arrays.asList((byte) 4, (byte) 4, (byte) 3));
        assertEquals(l3, RunLengthEncoder.decode(RunLengthEncoder.encode(l3)));
    }
    
    @Test
    public void encodeWorksOnTrivialArray(){
        List<Byte> l3 = new LinkedList<>(Arrays.asList((byte) 3));
        assertEquals(l3, RunLengthEncoder.encode(l3));
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void encodeThrowsException() {
        List<Byte> l3 = new LinkedList<>(Arrays.asList((byte) 3, (byte) 4, (byte) -2, (byte) 5));
        RunLengthEncoder.encode(l3);
    }
    
    @Test
    public void encodeWorksOnRandomArray(){
        List<Byte> l1 = new LinkedList<>(Arrays.asList((byte) 1, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0));
        List<Byte> t1 = new LinkedList<>(Arrays.asList((byte) 1, (byte) 0, (byte) -1, (byte) 1, (byte) 0, (byte) 1, (byte) -1, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) -2, (byte) 0));
        assertEquals(t1, RunLengthEncoder.encode(l1));
    }
    
    @Test
    public void encodeWorksOnLongSequencesArray(){
        List<Byte> l1 = new LinkedList<>(Arrays.asList((byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2,
                (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2,
                (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2,
                (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2,
                (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2,
                (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2,
                (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2,
                (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2,
                (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2,
                (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2,
                (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2,
                (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2,
                (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2,
                (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2));
        List<Byte> t1 = new LinkedList<>(Arrays.asList((byte) -128, (byte) 2, (byte) -8, (byte) 2));
        assertEquals(t1, RunLengthEncoder.encode(l1));
    }
    
    @Test
    public void decodeWorksProperlyWithEncodeArrays(){
        List<Byte> l = new LinkedList<>(Arrays.asList((byte) 3, (byte) 3, (byte) 3, (byte) 3));
        assertEquals(l, RunLengthEncoder.decode(RunLengthEncoder.encode(l)));
        
        List<Byte> l1 = new LinkedList<>(Arrays.asList((byte) 3, (byte) 3, (byte) 3, (byte) 3 ,(byte) 4));
        assertEquals(l1, RunLengthEncoder.decode(RunLengthEncoder.encode(l1)));
        
        List<Byte> l2 = new LinkedList<>(Arrays.asList((byte) 3, (byte) 3, (byte) 4, (byte) 4));
        assertEquals(l2, RunLengthEncoder.decode(RunLengthEncoder.encode(l2)));
        
        List<Byte> l3 = new LinkedList<>(Arrays.asList((byte) 4, (byte) 3, (byte) 3));
        assertEquals(l3, RunLengthEncoder.decode(RunLengthEncoder.encode(l3)));
        
        List<Byte> l4 = new LinkedList<>(Arrays.asList((byte) 4, (byte) 3));
        assertEquals(l4, RunLengthEncoder.decode(RunLengthEncoder.encode(l4)));
        
        List<Byte> l5 = new LinkedList<>(Arrays.asList((byte) 3, (byte) 3, (byte) 4, (byte) 5, (byte) 5));
        assertEquals(l5, RunLengthEncoder.decode(RunLengthEncoder.encode(l5)));
        
        List<Byte> l6 = new LinkedList<>(Arrays.asList((byte) 5, (byte) 3, (byte) 3, (byte) 4));
        assertEquals(l6, RunLengthEncoder.decode(RunLengthEncoder.encode(l6)));
        
        List<Byte> l7 = new LinkedList<>(Arrays.asList((byte) 3));
        assertEquals(l7, RunLengthEncoder.decode(RunLengthEncoder.encode(l7)));
        
        List<Byte> l8 = new LinkedList<>(Arrays.asList((byte) 1, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0));
        assertEquals(l8, RunLengthEncoder.decode(RunLengthEncoder.encode(l8)));
    }
    
    @Test (expected=NullPointerException.class)
    public void encodeThrowsNullPointerException(){
        RunLengthEncoder.encode(new LinkedList<Byte>());
    }
    
    @Test (expected=NullPointerException.class)
    public void decodeThrowsNullPointerException(){
        RunLengthEncoder.decode(new LinkedList<Byte>());
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void decodeThrowsException(){
        List<Byte> l = new LinkedList<>(Arrays.asList((byte) 2, (byte) -3));
        RunLengthEncoder.decode(l);
    }
    
    @Test
    public void decodeWorksOnRandomArray(){
        List<Byte> l1 = new LinkedList<>(Arrays.asList((byte) 1, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0));
        List<Byte> t1 = new LinkedList<>(Arrays.asList((byte) 1, (byte) 0, (byte) -1, (byte) 1, (byte) 0, (byte) 1, (byte) -1, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 1, (byte) -2, (byte) 0));
        assertEquals(l1, RunLengthEncoder.decode(t1));
    }

}
