package com.iut.threedviewer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;

public class Objet {
	private int nbPoints, nbSegments, nbTriangles;
	private List<List<Integer>> lPoints;
	private List<List<Integer>> lSegments;
	private List<List<Integer>> lTriangles;
    
	public Objet(String fichier)
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
    
    public void supprimerContenu(String destination)
    {
        new FileWriter(new File(destination)).close();
    }
    
    public void ajouterLigne(String txt, String destination)
    {
        FileWriter writer = null;
        try
        {
            writer = new FileWriter(destination, true);
            writer.write(txt,0,txt.length());
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if(writer != null)
            {
                writer.close();
            }
        }
    }
    
    public void deplacement(double x, double y, String destination)
    {
        supprimerContenu(destination);
        
        Iterator itr1 = this.lPoints.iterator();
        while(itr1.hasNext())
        {
            ArrayList<Double> point = itr1.next();
            double x_new = x + point.get(0)
            double y_new = y + point.get(1)
            ajouterLigne(x_new.toString()+" "+y_new.toString());
        }
        
        Iterator itr2 = this.lSegments.iterator();
        while(itr2.hasNext())
        {
            ArrayList<Integer> segment = itr2.next();
            ajouterLigne(segment.get(0).toString()+" "+segment.get(1).toString());
        }
        
        Iterator itr3 = this.lSegments.iterator();
        while(itr3.hasNext())
        {
            ArrayList<Integer> triangle = itr3.next();
            ajouterLigne(triangle.get(0).toString()+" "+triangle.get(1).toString()+" "+triangle.get(2).toString());
        }
    }
    
    public void rotation(int degrees, String destination)
    {
        supprimerContenu();
        
        
    }
    
}