package bastar;

/**
 * A graf egy pontjat reprezentalo osztaly.
 * @author AR
 */
public class Node
{
	//Konstansok a Node allapotanak tarolasahoz
	public static final int DEFAULT = 0;
	public static final int OPEN_SET = 1;
	public static final int CLOSED_SET = 2;
	public static final int STEPPED_ON = 3;
	public static final int BLOCKED = 4;
	public static final int STARTING = 5;
	public static final int GOAL = 6;

	private int mNodeState;
	private Node mPreviousNode;
	private double mGScore;
	private double mFScore;
	private int mRow;
	private int mCol;
	private int mOrder;

	/**
	 * Default konstruktor, amely beallitja a Node allapotat es a koordinatait.
	 * Lehetseges ertekei az osztaly statikus konstansai
	 * @param xNodeState Node allapota
	 */
	public Node( int xNodeState, int xRow, int xCol )
	{
		mNodeState = xNodeState;
		mRow = xRow;
		mCol = xCol;
		mOrder = -1;
		mFScore = 10000;
		mGScore = 10000;
		mPreviousNode = null;
	}

	/**
	 * Visszater a Node allapotaval.
	 * @return Node allapota
	 */
	public int getNodeState()
	{
		return mNodeState;
	}

	/**
	 * Beallitja a Node allapotat.
	 * Lehetseges ertekei a Node static konstansai.
	 * @param xNodeState a Node uj allapota
	 */
	public void setNodeState( int xNodeState )
	{
		mNodeState = xNodeState;
	}

	/**
	 * Visszater az algoritmus futasa soran beallitott,
	 * elozo Node referenciajaval.
	 * @return Elozo Node. Null, ha az algoritmus soran nem kerult vizsgalatra.
	 */
	public Node getPreviousNode()
	{
		return mPreviousNode;
	}

	/**
	 * Beallitja az elozo Node referenciajat.
	 * @param xPreviousNode elozo Node
	 */
	public void setPreviousNode( Node xPreviousNode )
	{
		mPreviousNode = xPreviousNode;
	}

	/**
	 * Visszater a tavolsag ertekevel.
	 * @return g() ertek
	 */
	public double getGScore()
	{
		return mGScore;
	}

	/**
	 * Beallitja a tavolsag erteket.
	 * @param xGScore g() ertek
	 */
	public void setGScore( double xGScore )
	{
		mGScore = xGScore;
	}

	/**
	 * Visszater a tavolsag es a heurisztika osszegevel.
	 * @return f() ertek
	 */
	public double getFScore()
	{
		return mFScore;
	}

	/**
	 * Beallitja a tavolsag es a heurisztika osszeget.
	 * @param xFScore f() ertek
	 */
	public void setFScore( double xFScore )
	{
		mFScore = xFScore;
	}

	/**
	 * Visszaadja a Node soranak szamat.
	 * @return Node sorszama
	 */
	public int getRow()
	{
		return mRow;
	}

	/**
	 * Visszaadja a Node oszlopanak a szamat.
	 * @return Node oszlopszama
	 */
	public int getCol()
	{
		return mCol;
	}

	/**
	 * Visszaadja, hogy hanyadikkent volt vizsgalva a Node az algoritmus soran.
	 * @return node sorszama
	 */
	public int getOrder()
	{
		return mOrder;
	}

	/**
	 * Beallitja, hogy hanyadikkent volt vizsgalva a Node az algoritmus soran.
	 * @param xOrder node sorszama
	 */
	public void setOrder( int xOrder )
	{
		mOrder = xOrder;
	}
}
