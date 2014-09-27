package bastar;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;

/**
 * Az alkalmazas belepesi pontja, amin keresztul az algoritmusunk
 * hivodik, es ezt a GUI eleri.
 * @author AR
 */
public class Framework
{
	static NodeMatrix test;
	static MainGui mGUI;
	static ExecutorService mExecutor;
	static long mStartTime = 0;
	static long mEndTime = 0;
	static int mBiggestStep = 0;
	static boolean mIsAnimated = true;
	static boolean mIsBidirectional = true;
	static boolean mAllowDiagonalStep = false;

	public static void main( String[] xArgs )
	{
		test = new NodeMatrix( "inputMatrix.txt" );
		mExecutor = Executors.newFixedThreadPool( 2 );
		mGUI = new MainGui( test );
	}

	public static void runAstar()
	{
		notifyStart();
		test.reset();
		Set< Node > closedSet = new HashSet< Node >();

		mExecutor.submit( new AStarThread( test, test.getStartingNode(), test.getEndingNode(), true ) );
		if ( mIsBidirectional )
		{
			mExecutor.submit( new AStarThread( test, test.getEndingNode(), test.getStartingNode(), false ) );
		}
	}

	public static synchronized void updateGUI()
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				mGUI.updateLabels();
			}
		} );
	}

	public static synchronized void notifyStart()
	{
		mBiggestStep = 0;
		mStartTime = System.currentTimeMillis();
		mGUI.enableSidePanel( false );
	}

	public static synchronized void notifyEnd( int xBiggestStep )
	{
		if ( xBiggestStep > mBiggestStep )
		{
			mBiggestStep = xBiggestStep;
		}
		mEndTime = System.currentTimeMillis();
		mGUI.enableSidePanel( true );
		mGUI.refreshStats();
	}
}
