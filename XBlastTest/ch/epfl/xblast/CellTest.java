package ch.epfl.xblast;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class CellTest {
    @Test
    public void rowMajorIndexCorrespondsToOrder() {
        int i = 0;
        for (Cell c: Cell.ROW_MAJOR_ORDER)
            assertEquals(i++, c.rowMajorIndex());
        assertEquals(Cell.COUNT, i);
    }

    @Test
    public void spiralOrderContainsAllCells() {
        assertEquals(Cell.COUNT, Cell.SPIRAL_ORDER.size());

        boolean[] cellSeen = new boolean[Cell.COUNT];
        for (Cell c: Cell.SPIRAL_ORDER) {
            assertFalse(cellSeen[c.rowMajorIndex()]);
            cellSeen[c.rowMajorIndex()] = true;
        }
    }

    @Test
    public void spiralOrderNeighborsAreSpatialNeighbors() {
        Cell pred = Cell.SPIRAL_ORDER.get(0);
        for (Cell c: Cell.SPIRAL_ORDER.subList(1, Cell.SPIRAL_ORDER.size())) {
            int areNeighborsCount = 0;
            for (Direction d: Direction.values()) {
                if (pred.equals(c.neighbor(d)))
                    areNeighborsCount += 1;
            }
            assertEquals(1, areNeighborsCount);
            pred = c;
        }
    }

    @Test
    public void constructorCorrectlyNormalizesCoordinates() {
        for (int i = -2; i <= 2; ++i) {
            Cell c = new Cell(14 + 15 * i, 12 + 13 * i);
            assertEquals(14, c.x());
            assertEquals(12, c.y());
        }
    }

    @Test
    public void neighborsOfOriginAreCorrect() {
        Cell c = new Cell(0, 0);
        assertEquals(new Cell( 0, 12), c.neighbor(Direction.N));
        assertEquals(new Cell( 1,  0), c.neighbor(Direction.E));
        assertEquals(new Cell( 0,  1), c.neighbor(Direction.S));
        assertEquals(new Cell(14,  0), c.neighbor(Direction.W));
    }

    @Test
    public void oppositeNeighborOfNeighborIsThis() {
        for (Cell c: Cell.ROW_MAJOR_ORDER) {
            for (Direction d: Direction.values()) {
                assertEquals(c, c.neighbor(d).neighbor(d.opposite()));
            }
        }
    }
    
    @Test
    public void equalsRedefinitionIsCorrect(){
        Cell c1 = new Cell(0,0); // Référence
        Cell c2 = new Cell(0,0); // Identique -> true
        Cell c3 = new Cell(1,1); // Différente -> false
        Direction d = Direction.N; // Classe différente -> false
        SubCell c4 = new SubCell(0,0); // Sous-classe -> false
        
        assertTrue(c1.equals(c2));
        assertFalse(c1.equals(c3));
        assertFalse(c1.equals(d));
        assertFalse(c1.equals(c4));
    }
    
    @Test
    public void toStringRedefinitionIsCorrect() {
        // Un test bidon qui n'échoue jamais, simplement pour tester l'affichage d'une cellule
        Cell c = new Cell(7, 3);
        System.out.println(c);
        assertTrue(true);
    }
    
    /*@Test
    public void spiralOrderIsCorrect() {
        // Ne pas oublier de mettre COLUMNS à 3 et ROWS à 2 pour le test
        ArrayList<Cell> cells = new ArrayList<Cell>();
        cells.add(new Cell(0,0));
        cells.add(new Cell(1,0));
        cells.add(new Cell(2,0));
        cells.add(new Cell(2,1));
        cells.add(new Cell(1,1));
        cells.add(new Cell(0,1));
        assertEquals(Cell.SPIRAL_ORDER, cells);
    }*/
}
