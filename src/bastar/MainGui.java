package bastar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * SWING GUI az algoritmus lepeseinek megjelenitesehez.
 * @author AR
 */
public class MainGui extends JFrame
{
	private NodeMatrix mNodeMatrix;
	private NodeLabel mLabelMatrix[][];
	private JPanel mNodeContainer;

	private JPanel mSidePanel;
	private JButton mRunButton;
	private JButton mResetButton;
	private JCheckBox mShowGridCheckBox;
	private JCheckBox mBidirechtionalCheckBox;
	private JCheckBox mAnimatedCheckBox;
	private JCheckBox mDiagonalCheckBox;

	private JLabel mRunTime;
	private JLabel mBiggestStep;

	private class NodeLabel extends JLabel
	{
		private Node mNode;

		public NodeLabel( Node xNode )
		{
			super( "" );
			mNode = xNode;
		}

		public Node getNode()
		{
			return mNode;
		}

		public void setNode( Node xNode )
		{
			mNode = xNode;
		}
	}

	private class StartButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Framework.runAstar();
		}
	}

	private class BidirectionalActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			Framework.mIsBidirectional = mBidirechtionalCheckBox.isSelected();
		}
	}

	private class AnimatedActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			Framework.mIsAnimated = mAnimatedCheckBox.isSelected();
		}
	}

	private class DiagonalActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			Framework.mAllowDiagonalStep = mDiagonalCheckBox.isSelected();
		}
	}

	private class ShowGridActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			updateLabels();
		}
	}

	public MainGui( NodeMatrix xNodeMatrix )
	{
		super( "AStar GUI" );
		mNodeMatrix = xNodeMatrix;
		setLayout( new BorderLayout() );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setSize( new Dimension( 800, 600 ) );
		initialize();
		updateLabels();
		pack();
		setVisible( true );
	}

	public void initialize()
	{
		int rows = mNodeMatrix.getRows();
		int cols = mNodeMatrix.getCols();
		mNodeContainer = new JPanel( new GridLayout( rows, cols ) );
		mNodeContainer.setBorder( BorderFactory.createLineBorder( Color.BLACK, 2 ) );

		mLabelMatrix = new NodeLabel[ rows ][ cols ];

		for ( int i = 0; i < rows; ++i )
		{
			for ( int j = 0; j < cols; ++j )
			{
				NodeLabel nl = new NodeLabel( mNodeMatrix.getNodeAtCoordinates( i, j ) );
				nl.setAlignmentX( CENTER_ALIGNMENT );
				nl.setOpaque( true );

				nl.setPreferredSize( new Dimension( 35, 35 ) );
				nl.setBorder( BorderFactory.createEmptyBorder() );
				mLabelMatrix[ i ][ j ] =  nl;
				mNodeContainer.add( mLabelMatrix[ i ][ j ] );
			}
		}
		JPanel marginPanel = new JPanel();
		marginPanel.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		marginPanel.add( mNodeContainer );
		add( marginPanel, BorderLayout.CENTER);

		mSidePanel = new JPanel();
		mSidePanel.setLayout( new BoxLayout( mSidePanel , 1 ) );
		mSidePanel.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		mRunButton = new JButton( "A* futtatása" );
		mRunButton.addActionListener( new StartButtonListener() );

		mBidirechtionalCheckBox = new JCheckBox( "Kétirányú", Framework.mIsBidirectional );
		mBidirechtionalCheckBox.addActionListener( new BidirectionalActionListener() );

		mDiagonalCheckBox = new JCheckBox( "Átlós lépés", Framework.mAllowDiagonalStep );
		mDiagonalCheckBox.addActionListener( new DiagonalActionListener() );

		mAnimatedCheckBox = new JCheckBox( "Lassítva", Framework.mIsAnimated );
		mAnimatedCheckBox.addActionListener( new AnimatedActionListener() );

		mShowGridCheckBox = new JCheckBox( "Rácsok mutatása" , false );
		mShowGridCheckBox.addActionListener( new ShowGridActionListener() );

		mBiggestStep = new JLabel( "0" );
		mRunTime = new JLabel( "0 ms" );

		JLabel runTimeLabel = new JLabel( "Elözö futási idö:" );
		runTimeLabel.setBorder( BorderFactory.createEmptyBorder( 50 , 0, 0, 0) );

		mSidePanel.add( mRunButton );
		mSidePanel.add( mBidirechtionalCheckBox );
		mSidePanel.add( mDiagonalCheckBox );
		mSidePanel.add( mAnimatedCheckBox );
		mSidePanel.add( mShowGridCheckBox );
		mSidePanel.add( runTimeLabel );
		mSidePanel.add( mRunTime );
		mSidePanel.add( new JLabel( "Elözö lépésszám:" ) );
		mSidePanel.add( mBiggestStep );
		add( mSidePanel, BorderLayout.EAST );
	}

	public void updateLabels()
	{
		int rows = mNodeMatrix.getRows();
		int cols = mNodeMatrix.getCols();

		for ( int i = 0; i < rows; ++i )
		{
			for ( int j = 0; j < cols; ++j )
			{
				NodeLabel nl = mLabelMatrix[ i ][ j ];
				Node node = mNodeMatrix.getNodeAtCoordinates( i , j );
				nl.setNode( node );

				if ( node.getOrder() > -1 )
				{
					nl.setText( node.getOrder() + "" );
				}
				else
				{
					nl.setText( "" );
				}

				if ( mShowGridCheckBox.isSelected() )
				{
					nl.setBorder( BorderFactory.createLineBorder( Color.LIGHT_GRAY, 1 ) );
				}
				else
				{
					nl.setBorder( BorderFactory.createEmptyBorder() );
				}

				switch ( node.getNodeState() )
				{
					case Node.BLOCKED		:	nl.setBackground( Color.BLACK ); break;
					case Node.STEPPED_ON	:	nl.setBackground( Color.GREEN ); break;
					case Node.GOAL			:	nl.setBackground( Color.RED ); break;
					case Node.STARTING		:	nl.setBackground( Color.RED ); break;
					case Node.OPEN_SET		:	nl.setBackground( Color.YELLOW ); break;
					case Node.CLOSED_SET	:	nl.setBackground( Color.ORANGE ); break;
					case Node.DEFAULT		:	nl.setBackground( Color.WHITE ); break;
				}
			}
		}
		revalidate();
	}

	public void enableSidePanel( boolean xEnabled )
	{
		for ( Component c : mSidePanel.getComponents() )
		{
			c.setEnabled( xEnabled );
		}
	}

	public void refreshStats()
	{
		long runTime = Framework.mEndTime - Framework.mStartTime;
		mRunTime.setText( runTime + " ms" );
		mBiggestStep.setText( Framework.mBiggestStep + "" );
	}
}
