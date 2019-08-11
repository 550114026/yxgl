package com.ikkong.utils;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class GisUtil {
	 /** 
     * 判断当前位置是否在围栏内 
     */  
      
    //dlist为某个多变形的所有点  
     public static  boolean isInPolygon(GisPoint gisPoint,List<GisPoint> dlist){  
         double p_x =gisPoint.getLng();  
         double p_y =gisPoint.getLat();  
         Point2D.Double point = new Point2D.Double(p_x, p_y);  
  
         List<Point2D.Double> pointList= new ArrayList<Point2D.Double>();  
           
         for (GisPoint gp : dlist){  
             Point2D.Double polygonPoint = new Point2D.Double(gp.getLng(),gp.getLat());  
             pointList.add(polygonPoint);  
         }  
         return GisUtil.checkWithJdkGeneralPath(point,pointList);  
     }  
     /** 
      * 返回一个点是否在一个多边形区域内 
      * @param point 
      * @param polygon 
      * @return 
      */  
    private static boolean checkWithJdkGeneralPath(Point2D.Double point, List<Point2D.Double> polygon) {  
           java.awt.geom.GeneralPath p = new java.awt.geom.GeneralPath();  
  
           Point2D.Double first = polygon.get(0);  
           p.moveTo(first.x, first.y);  
           polygon.remove(0);  
           for (Point2D.Double d : polygon) {  
              p.lineTo(d.x, d.y);  
           }  
           p.lineTo(first.x, first.y);  
           p.closePath();  
           return p.contains(point); 
        }  
}
