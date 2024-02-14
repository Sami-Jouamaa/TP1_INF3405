package Server;
import java.util.LinkedList;
import java.util.Queue;

public class LimitedSizeQueue<E> //classe reprise de https://www.geeksforgeeks.org/how-to-implement-size-limited-queue-that-holds-last-n-elements-in-java/
extends LinkedList<E> { 

	// Variable which store the 
	// SizeLimitOfQueue of the queue 
	private int SizeLimitOfQueue; 
  
	// Constructor method for initializing 
	// the SizeLimitOfQueue variable 
	public LimitedSizeQueue(int SizeLimitOfQueue) 
	{ 
		this.SizeLimitOfQueue = SizeLimitOfQueue; 
	} 
  
	// Override the method add() available 
	// in LinkedList class so that it allow 
	// addition  of element in queue till 
	// queue size is less than 
	// SizeLimitOfQueue otherwise it remove 
	// the front element of queue and add 
	// new element 
	@Override
	public boolean add(E o) 
	{ 
		while (this.size() == SizeLimitOfQueue) { 
			super.remove(); 
		} 
		super.add(o); 
		return true; 
	}
	
	public void afficherQueue()
	{
		for (int i = 0; i < SizeLimitOfQueue; i++)
		{
			System.out.println(this.get(i));
		}
	}
}
