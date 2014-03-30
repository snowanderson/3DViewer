package com.iut.threedviewer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Parser {
	private int nbPoints, nbSegments, nbTriangles;
	private List<List<Integer>> lPoints;
	private List<List<Integer>> lSegments;
	private List<List<Integer>> lTriangles;
    
	public Parser(String fichier)
    {
		try
        {
            InputStream ips=new FileInputStream(fichier);
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader buff=new BufferedReader(ipsr);
            String line = buff.readLine();
            
			nbPoints = Integer.parseInt(line.split(" ")[0]);
			nbSegments = Integer.parseInt(line.split(" ")[1]);
			nbTriangles = Integer.parseInt(line.split(" ")[2]);
			lPoints = new ArrayList<ArrayList<Double>>();
			lSegments = new ArrayList<ArrayList<Integer>>();
			lTriangles = new ArrayList<ArrayList<Integer>>();
            
            List<Integer> pointCourrant = new ArrayList<Integer>;
            List<Integer> segmentCourrant = new ArrayList<Integer>;
            List<Integer> triangleCourrant = new ArrayList<Integer>;
            
			int n = 0;
			double x, y, z; //coordonnees de point
			int a, b, c; //points du segment (a,b) ou segments du triangle (a,b,c)
			while ((line = buff.readLine()) != null)
            {
				n += 1;
				if (n <= nbPoints)
                {
					x = parseDouble(line.split(" ")[0]);
					y = parseDouble(line.split(" ")[1]);
					z = parseDouble(line.split(" ")[2]);
                    pointCourrant.add(x);
                    pointCourrant.add(y);
                    pointCourrant.add(z);
					lPoints.add(pointCourrant);
                    pointCourrant.clear();
				}
                else if (n <= (nbPoints + nbSegments))
                {
					a = parseInt(line.split(" ")[0]);
					b = parseInt(line.split(" ")[1]);
					segmentCourrant.add(a);
                    segmentCourrant.add(b);
					lSegments.add(segmentCourrant);
                    segmentCourrant.clear();
				}
                else if (n <= (nbPoints + nbSegments + nbTriangles))
                {
					a = parseInt(line.split(" ")[0]);
					b = parseInt(line.split(" ")[1]);
					c = parseInt(line.split(" ")[2]);
					triangleCourrant.add(a);
                    triangleCourrant.add(b);
                    triangleCourrant.add(c);
					lTriangles.add(triangleCourrant);
                    triangleCourrant.clear();
				}
			}
			buff.close();
		}
        catch (IOException e)
        {
			e.printStackTrace();
		}
	}
    
	public int getNbPoints()
    {
		return nbPoints;
	}
    
    public int getNbSegments()
    {
		return nbSegments;
	}
    
    public int getNbTriangles()
    {
		return nbTriangles;
	}
    
    public List<List<Double>> getPoints()
    {
		return lPoints;
	}
    
    public List<List<Integer>> getSegments()
    {
		return lSegments;
	}
    
    public List<List<Integer>> getTriangles()
    {
		return lSegments;
	}
    
	public void setNbPoints(int nb)
    {
		this.nbPoints = nb;
	}
    
	public void setNbSegments(int nb)
    {
		this.nbSegments = nb;
	}
    
	public void setNbTriangles(int nb)
    {
		this.nbTriangles = nb;
	}
    
	public void setPoints(List<List<Double>> l)
    {
		this.lPoints = l;
	}
    
	public void setSegments(List<List<Integer>> l)
    {
		this.lSegments = l;
	}
    
	public void setTriangles(List<List<Integer>> l)
    {
		this.lTriangles = l;
	}
    
}