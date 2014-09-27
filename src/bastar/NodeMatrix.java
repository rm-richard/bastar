package bastar;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * A NodeSet matrixos (negyzetracsos) implementacioja
 * @see NodeSet
 * @author AR
 */
public class NodeMatrix implements NodeSet
{
	private Node[][] mNodeMatrix;
	private int mRows;
	private int mCols;
	private Node mStartingNode;
	private Node mGoalNode;
	private Node mStoppedNode;

	/**
	 * Konstruktor, mely egy filebol inicializalja a graf matrixot.
	 * @see NodeMatrix#readMatrixFromFile(java.lang.String) 
	 * @param xInputFile input file
	 */
	public NodeMatrix( String xInputFile )
	{
		readMatrixFromFile( xInputFile );
	}

	/**
	 * Metodus, ami fajlbol beolvassa a NodeMatrixot.
	 * Elvart formatum: A Node matrix konstansainak megfelelo integerekbol
	 * allo sorok, azonos oszlop es sorszelesseggel.
	 * @see Node
	 * @param xInputFile input file
	 */
	public void readMatrixFromFile( String xInputFile )
	{
		mStoppedNode = null;
		mRows = 0;
		mCols = 0;
		File inputFile = new File( xInputFile );
		Scanner rowScanner = null;
		Scanner colScanner = null;

		try
		{
			rowScanner = new Scanner( inputFile );
			while ( rowScanner.hasNextLine() )
			{
				mRows += 1;
				String currentLine = rowScanner.nextLine();
				String[] numbers = currentLine.split( " " );
				mCols = numbers.length;
			}

			mNodeMatrix = new Node[ mRows ][ mCols ];

			rowScanner.close();

			rowScanner = new Scanner( inputFile );
			for ( int i = 0; i < mRows; ++i )
			{
				for ( int j = 0; j < mCols; ++j )
				{
					if ( rowScanner.hasNextInt() )
					{
						int nodeState = rowScanner.nextInt();
						Node newNode = new Node( nodeState, i, j );
						mNodeMatrix[ i ][ j ] = newNode;

						if ( nodeState == Node.STARTING)
						{
							mStartingNode = newNode;
						}
						else if ( nodeState == Node.GOAL )
						{
							mGoalNode = newNode;
						}
					}
				}
			}
		}
		catch ( IOException ex )
		{
			System.out.println( ex );
		}
		finally
		{
			if ( rowScanner != null )
			{
				rowScanner.close();
			}
		}
	}

	/**
	 * Kiiratja a matrixot a standard outputra.
	 * Debughoz hasznalatos.
	 */
	public void printMatrix( )
	{
		System.out.println( "Rows: " + mRows + " Cols: " +mCols );
		for ( int i = 0; i < mRows; ++i )
		{
			for ( int j = 0; j < mCols; ++j )
			{
				System.out.print( mNodeMatrix[ i ][ j ].getNodeState() + " " );
			}
			System.out.println();
		}
	}

	/**
	 * Visszater a kiindulasi ponttal.
	 * @return kiindulasi pont
	 */
	@Override
	public Node getStartingNode()
	{
		return mStartingNode;
	}

	/**
	 * Visszater a celponttal.
	 * @return celpont
	 */
	@Override
	public Node getEndingNode()
	{
		return mGoalNode;
	}

	@Override
	public Node getStoppedNode()
	{
		return mStoppedNode;
	}

	@Override
	public void setStoppedNode( Node xStoppedNode )
	{
		if ( mStoppedNode == null )
		{
			mStoppedNode = xStoppedNode;
		}
	}

	/**
	 * Visszater a parameterkent megadott Node szomszedaival.
	 * Atlos szomszedokat nem ad vissza.
	 * @param xNode aminek a szomszedait akarjuk
	 * @return a Node szomszedait tartalmazo Set
	 */
	@Override
	public Set< Node > getNeighbours( Node xNode )
	{
		Set< Node > neighbourSet = new HashSet< Node >();
		int rowPos = xNode.getRow();
		int colPos = xNode.getCol();

		//balra levo elem
		if ( rowPos > 0 )
		{
			neighbourSet.add( mNodeMatrix[ rowPos - 1 ][ colPos ] );
		}
		//jobbra levo elem
		if ( rowPos < mRows - 1 )
		{
			neighbourSet.add( mNodeMatrix[ rowPos + 1 ][ colPos ] );
		}
		//felette levo elem
		if ( colPos > 0 )
		{
			neighbourSet.add( mNodeMatrix[ rowPos ][ colPos - 1 ] );
		}
		//alatta levo elem
		if ( colPos < mCols - 1 )
		{
			neighbourSet.add( mNodeMatrix[ rowPos ][ colPos + 1 ] );
		}

		//ha megengedjuk az atlos lepeseket is
		if ( Framework.mAllowDiagonalStep )
		{
			if ( rowPos > 0 && colPos > 0 )
			{
				neighbourSet.add( mNodeMatrix[ rowPos - 1 ][ colPos - 1 ] );
			}

			if ( rowPos > 0 && colPos < mCols - 1 )
			{
				neighbourSet.add( mNodeMatrix[ rowPos - 1 ][ colPos + 1 ] );
			}

			if ( rowPos < mRows - 1 && colPos > 0 )
			{
				neighbourSet.add( mNodeMatrix[ rowPos + 1 ][ colPos - 1 ] );
			}

			if ( rowPos < mRows - 1 && colPos < mCols - 1 )
			{
				neighbourSet.add( mNodeMatrix[ rowPos + 1 ][ colPos + 1 ] );
			}
		}

		return neighbourSet;
	}

	@Override
	public double calculateHeuristic( Node xNode, Boolean xIsForward )
	{
		int rowPos = xNode.getRow();
		int colPos = xNode.getCol();

		int startRow = mStartingNode.getRow();
		int startCol = mStartingNode.getCol();

		int goalRow = mGoalNode.getRow();
		int goalCol = mGoalNode.getCol();

		double distFromStart = Math.sqrt( ( rowPos - startRow ) * ( rowPos - startRow ) 
						+ ( colPos - startCol ) * ( colPos - startCol ) );

		double distFromEnd = Math.sqrt( ( rowPos - goalRow ) * ( rowPos - goalRow ) 
						+ ( colPos - goalCol ) * ( colPos - goalCol ) );

		//ekkor elore iranyu
		if ( xIsForward )
		{
			return ( distFromEnd - distFromStart ) / 2;
		}
		else
		{
			return ( distFromStart - distFromEnd ) / 2;
		}

		//MANHATTAN
		//return Math.abs( distX ) + Math.abs( distY );

		//EUCLIDEAN
		//return Math.sqrt( ( rowPos - goalRow ) * ( rowPos - goalRow ) 
		//				+ ( colPos - goalCol ) * ( colPos - goalCol ) );
	}

	/**
	 * Kihasznaljuk, hogy csak szomszedos Node-okra hivjuk az algoritmusban.
	 * Tetszoleges Node-okra nem ad jo eredmenyt.
	 */
	@Override
	public double calculateDistance( Node xNode1, Node xNode2 )
	{
		int rowPos1 = xNode1.getRow();
		int colPos1 = xNode1.getCol();

		int rowPos2 = xNode2.getRow();
		int colPos2 = xNode2.getCol();

		double distanceValue = 1.0;

		//ha atlos, akkor tobb a distance
		if ( rowPos1 != rowPos2 && colPos1 != colPos2 )
		{
			distanceValue = 1.4;
		}

		return distanceValue;
	}

	@Override
	public void reset()
	{
		mStoppedNode = null;
		for ( int i = 0; i < mRows; ++i )
		{
			for ( int j = 0; j < mCols; ++j )
			{
				if ( mNodeMatrix[ i ][ j ].getNodeState() != Node.STARTING
						&& mNodeMatrix[ i ][ j ].getNodeState() != Node.GOAL
						&& mNodeMatrix[ i ][ j ].getNodeState() != Node.BLOCKED )
				{
					mNodeMatrix[ i ][ j ].setNodeState( Node.DEFAULT );
				}
				int state = mNodeMatrix[ i ][ j ].getNodeState();
				Node newNode = new Node( state, i, j );
				mNodeMatrix[ i ][ j ] = newNode;
				if ( newNode.getNodeState() == Node.STARTING )
				{
					mStartingNode = newNode;
				}
				if ( newNode.getNodeState() == Node.GOAL )
				{
					mGoalNode = newNode;
				}
				
			}
		}
	}

	/**
	 * Visszater az adott koordinatan talalhato Node-dal.
	 * @param xRow sor
	 * @param xCol oszlop
	 * @return Node
	 */
	public Node getNodeAtCoordinates( int xRow, int xCol )
	{
		return mNodeMatrix[ xRow ][ xCol ];
	}

	public int getRows()
	{
		return mRows;
	}

	public int getCols()
	{
		return mCols;
	}
}
