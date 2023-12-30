//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.geoserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.geoserver.wps.gs.GeoServerProcess;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.DirectPosition;

@DescribeProcess(
        title = "pointExtraction",
        description = "栅格提取到点"
)
public class PointExtraction implements GeoServerProcess {
    public PointExtraction() {
    }

    @DescribeResult(
            name = "outputFeatures",
            description = "输出矢量数据"
    )
    public SimpleFeatureCollection execute(@DescribeParameter(name = "inputFeatures",description = "输入矢量数据") SimpleFeatureCollection pointshp,
                                           @DescribeParameter(name = "inputRaster1",description = "输入遥感影像",max = 1,min = 1) GridCoverage2D raster1,
                                           @DescribeParameter(name = "inputRasterName1",description = "输入对应字段名",max = 1,min = 1) String rasterName1,
                                           @DescribeParameter(name = "inputRaster2",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster2,
                                           @DescribeParameter(name = "inputRasterName2",description = "输入对应字段名",max = 1,min = 0) String rasterName2,
                                           @DescribeParameter(name = "inputRaster3",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster3,
                                           @DescribeParameter(name = "inputRasterName3",description = "输入对应字段名",max = 1,min = 0) String rasterName3,
                                           @DescribeParameter(name = "inputRaster4",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster4,
                                           @DescribeParameter(name = "inputRasterName4",description = "输入对应字段名",max = 1,min = 0) String rasterName4,
                                           @DescribeParameter(name = "inputRaster5",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster5,
                                           @DescribeParameter(name = "inputRasterName5",description = "输入对应字段名",max = 1,min = 0) String rasterName5,
                                           @DescribeParameter(name = "inputRaster6",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster6,
                                           @DescribeParameter(name = "inputRasterName6",description = "输入对应字段名",max = 1,min = 0) String rasterName6,
                                           @DescribeParameter(name = "inputRaster7",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster7,
                                           @DescribeParameter(name = "inputRasterName7",description = "输入对应字段名",max = 1,min = 0) String rasterName7,
                                           @DescribeParameter(name = "inputRaster8",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster8,
                                           @DescribeParameter(name = "inputRasterName8",description = "输入对应字段名",max = 1,min = 0) String rasterName8,
                                           @DescribeParameter(name = "inputRaster9",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster9,
                                           @DescribeParameter(name = "inputRasterName9",description = "输入对应字段名",max = 1,min = 0) String rasterName9,
                                           @DescribeParameter(name = "inputRaster10",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster10,
                                           @DescribeParameter(name = "inputRasterName10",description = "输入对应字段名",max = 1,min = 0) String rasterName10) throws Exception {
        List<GridCoverage2D> rasterList = new ArrayList();
        List<String> rasterNameList = new ArrayList();
        rasterList.add(raster1);
        rasterNameList.add(rasterName1);
        if (raster2 != null) {
            rasterList.add(raster2);
            rasterNameList.add(rasterName2);
        }
        if (raster3 != null) {
            rasterList.add(raster3);
            rasterNameList.add(rasterName3);
        }

        if (raster4 != null) {
            rasterList.add(raster4);
            rasterNameList.add(rasterName4);
        }
        if (raster5 != null) {
            rasterList.add(raster5);
            rasterNameList.add(rasterName5);
        }

        if (raster6 != null) {
            rasterList.add(raster6);
            rasterNameList.add(rasterName6);
        }

        if (raster7 != null) {
            rasterList.add(raster7);
            rasterNameList.add(rasterName7);
        }
        if (raster8 != null) {
            rasterList.add(raster8);
            rasterNameList.add(rasterName8);
        }

        if (raster9 != null) {
            rasterList.add(raster9);
            rasterNameList.add(rasterName9);
        }

        if (raster10 != null) {
            rasterList.add(raster10);
            rasterNameList.add(rasterName10);
        }
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("PointWithRasterValue");
        builder.addAll(((SimpleFeatureType)pointshp.getSchema()).getAttributeDescriptors());
        for (String rasterName : rasterNameList) {
            builder.add(rasterName, Float.class);
        }

        SimpleFeatureType newType = builder.buildFeatureType();
        System.out.println(newType);
        List<SimpleFeature> features = new ArrayList();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(newType);
        SimpleFeatureIterator itr = pointshp.features();
        Throwable var12 = null;

        try {
            while(itr.hasNext()) {
                SimpleFeature pointFeature = (SimpleFeature)itr.next();
                Geometry geom = (Geometry)pointFeature.getDefaultGeometry();
                if (geom instanceof Point) {
                    DirectPosition pointPos = new DirectPosition2D(geom.getCoordinate().x, geom.getCoordinate().y);
                    featureBuilder.addAll(pointFeature.getAttributes());

                    float rasterValue;
                    for(Iterator var16 = rasterList.iterator(); var16.hasNext(); featureBuilder.add(rasterValue)) {
                        GridCoverage2D raster = (GridCoverage2D)var16.next();
                        rasterValue = 0.0F;
                        Envelope2D envelope = raster.getEnvelope2D();
                        if (envelope.contains(pointPos)) {
                            float[] dest = new float[1];
                            Object valueObj = raster.evaluate(pointPos, dest);
                            if (valueObj instanceof float[]) {
                                rasterValue = dest[0];
                            }
                        }
                    }

                    SimpleFeature newFeature = featureBuilder.buildFeature((String)null);
                    features.add(newFeature);
                }
            }
        } catch (Throwable var29) {
            var12 = var29;
            throw var29;
        } finally {
            if (itr != null) {
                if (var12 != null) {
                    try {
                        itr.close();
                    } catch (Throwable var28) {
                        var12.addSuppressed(var28);
                    }
                } else {
                    itr.close();
                }
            }

        }

        return DataUtilities.collection(features);
    }
}
