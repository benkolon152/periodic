package com.example.periodic;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class PeriodicTableControllerTest {

    @BeforeClass
    public static void initToolkit() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        try {
            // initialize JavaFX toolkit once; the Runnable runs on the FX thread after startup
            Platform.startup(latch::countDown);
        } catch (IllegalStateException ex) {
            // already initialized — mark latch so tests proceed
            latch.countDown();
        }
        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Timeout initializing JavaFX Platform");
        }
    }

    private static void runOnFxThread(Runnable action) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        });
        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Timeout waiting for FX thread");
        }
    }

    @Test
    public void testReadCsvLoadsAllElements() {
        PeriodicTableController controller = new PeriodicTableController();
        controller.readCsv("periodic-table-data.csv");
        assertNotNull("elements list should not be null", controller.elements);
        assertEquals("Expected 118 elements loaded from CSV", 118, controller.elements.size());
        assertEquals(1, controller.elements.get(0).atomicNumber);
        assertEquals(118, controller.elements.get(117).atomicNumber);
    }

    @Test
    public void testGetCoordsForAtomicNumber() throws Exception {
        PeriodicTableController controller = new PeriodicTableController();
        Method m = PeriodicTableController.class.getDeclaredMethod("getCoordsForAtomicNumber", int.class);
        m.setAccessible(true);

        Integer[] c1 = (Integer[]) m.invoke(controller, 1);
        assertArrayEquals(new Integer[]{1, 1}, c1);

        Integer[] he = (Integer[]) m.invoke(controller, 2);
        assertArrayEquals(new Integer[]{1, 18}, he);

        Integer[] ce = (Integer[]) m.invoke(controller, 58);
        assertArrayEquals(new Integer[]{8, 4}, ce);

        Integer[] th = (Integer[]) m.invoke(controller, 90);
        assertArrayEquals(new Integer[]{9, 4}, th);

        Integer[] fe = (Integer[]) m.invoke(controller, 26);
        assertArrayEquals(new Integer[]{4, 8}, fe);
    }

    @Test
    public void testAddElementAddsCellToGridPane() throws Exception {
        PeriodicTableController controller = new PeriodicTableController();
        GridPane grid = new GridPane();

        runOnFxThread(() -> {
            controller.addElement(grid, 1, 1, 1, javafx.scene.paint.Color.LIGHTGRAY, "H", "Hydrogen");
        });

        assertEquals("Grid should contain one child after addElement", 1, grid.getChildren().size());
        Node n = grid.getChildren().get(0);
        assertTrue("Added node should be a StackPane", n instanceof StackPane);

        Integer col = GridPane.getColumnIndex(n);
        Integer row = GridPane.getRowIndex(n);
        int colIdx = (col == null) ? 0 : col;
        int rowIdx = (row == null) ? 0 : row;
        assertEquals(0, colIdx);
        assertEquals(0, rowIdx);

        StackPane cell = (StackPane) n;
        boolean hasRectangle = cell.getChildren().stream().anyMatch(ch -> ch instanceof javafx.scene.shape.Rectangle);
        assertTrue("Cell should contain a Rectangle background", hasRectangle);
    }
}