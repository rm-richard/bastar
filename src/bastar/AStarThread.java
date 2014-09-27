package bastar;

import java.util.HashSet;
import java.util.Set;

/**
 *A* kereses implementacioja
 * @author AR
 */
public class AStarThread implements Runnable
{
	/**
	 * Kiinduasi graf (NodeSet), amin az algoritmus dolgozik.
	 */
	NodeSet mStartingSet;

	/**
	 * Kiindulasi Node
	 */
	Node mStartingNode;

	/**
	 * Celnode
	 */
	Node mGoalNode;

	/**
	 * Mar megvizsgalt Node-ok.
	 */
	Set< Node > mClosedSet;

	/**
	 * Vizsgalando Node-ok.
	 */
	Set< Node > mOpenSet;

	Boolean mIsForward;

	static Object mStopLock = new Object();

	/**
	 * Konstruktor.
	 * Inicializalja a tagvaltozokat.
	 * @param xStartingSet Kiindulasi graf
	 * @param xStartingNode Kiindulasi Node
	 * @param xGoalNode celnode
	 */
	public AStarThread( NodeSet xStartingSet, Node xStartingNode, Node xGoalNode, Boolean xIsForward )
	{
		mStartingSet = xStartingSet;
		mClosedSet = new HashSet< Node >();
		mOpenSet = new HashSet< Node >();

		mStartingNode = xStartingNode;
		mGoalNode = xGoalNode;

		mOpenSet.add( xStartingNode );
		mIsForward = xIsForward;
	}

	/**
	 * A* algoritmus.
	 */
	public void aStar()
	{
		int step = 0;

		try
		{
			Node currentNode =mStartingNode;
			currentNode.setGScore( 0 );
			currentNode.setFScore( 0 + mStartingSet.calculateHeuristic( currentNode, mIsForward ) );
			currentNode.setOrder( step );

			while ( !mOpenSet.isEmpty() )
			{
				//minimum F keresese az open setben
				Node min = null;
				for ( Node n : mOpenSet )
				{
					if ( min == null )
					{
						min = n;
					}
					else if ( min.getFScore() > n.getFScore() )
					{
						min = n;
					}
				}
				currentNode = min;

				mOpenSet.remove( currentNode );

				if ( currentNode.getNodeState() != Node.STARTING && currentNode.getNodeState() != Node.GOAL )
				{
					currentNode.setNodeState( Node.CLOSED_SET );
				}

				Set< Node > neighbors = mStartingSet.getNeighbours( currentNode );
				for ( Node n : neighbors )
				{
					synchronized ( n )
					{
						synchronized ( mStopLock )
						{
							if ( mStartingSet.getStoppedNode() != null )
							{
								reconstructPath( mStartingSet.getStoppedNode() );
								return;
							}

							if ( n == mGoalNode
								|| ( n.getNodeState() == Node.CLOSED_SET && !mClosedSet.contains( n ) ) 
								|| ( n.getNodeState() == Node.OPEN_SET && !mOpenSet.contains( n ) ) )
							{
								mStartingSet.setStoppedNode( n );
								reconstructPath( currentNode );
								return;
							}
						}

						double gScore = currentNode.getGScore() + mStartingSet.calculateDistance( currentNode , n );
						double fScore = gScore + mStartingSet.calculateHeuristic( n, mIsForward );

						if ( n.getNodeState() == Node.BLOCKED )
						{
							continue;
						}

						if ( mClosedSet.contains( n ) && fScore >= n.getFScore() )
						{
							continue;
						}

						/*if ( currentNode == mGoalNode
							|| ( n.getNodeState() == Node.CLOSED_SET && !mClosedSet.contains( n ) ) 
							|| ( n.getNodeState() == Node.OPEN_SET && !mOpenSet.contains( n ) ) )
						{
							mStartingSet.setStoppedNode( n );
							reconstructPath( currentNode );
							return;
						}*/

						int state = n.getNodeState();
						if ( ( !mOpenSet.contains( n ) || fScore < n.getFScore() )
								&& state != Node.OPEN_SET )
						{
							n.setPreviousNode( currentNode );
							n.setFScore( fScore );
							n.setGScore( gScore );
							mOpenSet.add( n );
							n.setOrder( ++step );
							if ( n.getNodeState() != Node.STARTING
									&& n.getNodeState() != Node.GOAL
									&& n.getNodeState() != Node.CLOSED_SET )
							{
								n.setNodeState( Node.OPEN_SET );
							}
						}
					}

					try
					{
						Framework.updateGUI();
						if ( Framework.mIsAnimated )
						{
							Thread.sleep( 100 );
						}
						else
						{
							//utemezes egyenletessege miatt
							Thread.sleep( 1 );
						}
					}
					catch ( InterruptedException ex )
					{

					}
				}

				mClosedSet.add( currentNode );
			}
		}
		finally
		{
			Framework.notifyEnd( step );
		}
	}

	/**
	 * Az algoritmus altal megtalalt utat visszaallito metodus.
	 * @param xEnd melyik pontban vegzodott az algoritmus
	 */
	public static void reconstructPath( Node xEnd )
	{
		Node current = xEnd;

		while ( current != null )
		{
			if ( current.getNodeState() != Node.STARTING
				&& current.getNodeState() != Node.GOAL
				&& current.getNodeState() != Node.STEPPED_ON )
			{
				current.setNodeState( Node.STEPPED_ON );
			}
			current = current.getPreviousNode();
		}
		Framework.updateGUI();
	}

	@Override
	public void run()
	{
		aStar();
	}
}
