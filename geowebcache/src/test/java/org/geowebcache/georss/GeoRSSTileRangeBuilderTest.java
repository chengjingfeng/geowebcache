package org.geowebcache.georss;

import static org.geowebcache.georss.GeoRSSTestUtils.buildSampleFilterMatrix;
import junit.framework.TestCase;

import org.geowebcache.grid.BoundingBox;
import org.geowebcache.grid.GridSetBroker;
import org.geowebcache.layer.TileLayer;
import org.geowebcache.util.TestUtils;

public class GeoRSSTileRangeBuilderTest extends TestCase {

    /**
     * Use the System property {@code org.geowebcache.debugToDisk} in order for the mask images
     * produces to be logged to the target directory
     */
    private static final boolean debugToDisk = Boolean.getBoolean("org.geowebcache.debugToDisk");

    private TileLayer layer;

    private String gridsetId;

    public void setUp() {
        GeoRSSTestUtils.debugToDisk = debugToDisk;
        layer = TestUtils.createWMSLayer("image/png", new GridSetBroker(false, false), 3, 3,
                new BoundingBox(-180, -90, 180, 90));
        gridsetId = layer.getGridSubsets().keySet().iterator().next();
    }

    public void testBuildTileRangeMask() throws Exception {

        TileGridFilterMatrix tileRangeMask = buildSampleFilterMatrix(layer, gridsetId);

        assertNotNull(tileRangeMask);
        assertEquals(0, tileRangeMask.getStartLevel());
        assertEquals(11, tileRangeMask.getNumLevels());
        assertEquals(layer.getGridSubset(gridsetId).getCoverages().length, tileRangeMask
                .getNumLevels());

    }

    /**
     * Test for {@link TileGridFilterMatrix#getCoveredBounds(int)}
     * 
     * @throws Exception
     */
    public void testCoveredBounds() throws Exception {
        TileGridFilterMatrix tileRangeMask = buildSampleFilterMatrix(layer, gridsetId);

        long[][] coverages = layer.getGridSubset(gridsetId).getCoverages();
        long[][] expectedGridCoverages = {// just as a reminder
        new long[] { 0, 0, 1, 0, 0 },//
                new long[] { 0, 0, 3, 1, 1 },//
                new long[] { 0, 0, 7, 3, 2 },//
                new long[] { 0, 0, 15, 7, 3 },//
                new long[] { 0, 0, 31, 15, 4 },//
                new long[] { 0, 0, 63, 31, 5 },//
                new long[] { 0, 0, 127, 63, 6 },//
                new long[] { 0, 0, 255, 127, 7 },//
                new long[] { 0, 0, 511, 255, 8 },//
                new long[] { 0, 0, 1023, 511, 9 },//
                new long[] { 0, 0, 2047, 1023, 10 } //
        };
        TestUtils.assertEquals(expectedGridCoverages, coverages);

        TestUtils.assertEquals(new long[] { 0, 0, 1, 0, 0 }, tileRangeMask.getCoveredBounds(0));
        TestUtils.assertEquals(new long[] { 0, 0, 3, 1, 1 }, tileRangeMask.getCoveredBounds(1));
        TestUtils.assertEquals(new long[] { 1, 0, 7, 3, 2 }, tileRangeMask.getCoveredBounds(2));
        TestUtils.assertEquals(new long[] { 3, 0, 15, 6, 3 }, tileRangeMask.getCoveredBounds(3));
        TestUtils.assertEquals(new long[] { 7, 0, 31, 12, 4 }, tileRangeMask.getCoveredBounds(4));
        TestUtils.assertEquals(new long[] { 15, 0, 63, 24, 5 }, tileRangeMask.getCoveredBounds(5));
        TestUtils.assertEquals(new long[] { 31, 0, 127, 48, 6 }, tileRangeMask.getCoveredBounds(6));
        TestUtils.assertEquals(new long[] { 63, 0, 255, 96, 7 }, tileRangeMask.getCoveredBounds(7));
        TestUtils.assertEquals(new long[] { 127, 0, 511, 192, 8 }, tileRangeMask
                .getCoveredBounds(8));
        TestUtils.assertEquals(new long[] { 255, 0, 1023, 384, 9 }, tileRangeMask
                .getCoveredBounds(9));
        TestUtils.assertEquals(new long[] { 511, 0, 2047, 768, 10 }, tileRangeMask
                .getCoveredBounds(10));
    }

}