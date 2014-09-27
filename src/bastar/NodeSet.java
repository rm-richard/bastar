package bastar;

import java.util.Set;

/**
 * Az A* algoritmus altal hasznalt adatstruktura interfesze.
 * @author AR
 */
public interface NodeSet
{
	/**
	 * Az algoritmus indulasi pontjat adja vissza.
	 * @return indulasi pont
	 */
	public Node getStartingNode();

	/**
	 * Az algoritmus celpontjat adja vissza.
	 * @return celpont
	 */
	public Node getEndingNode();

	public  Node getStoppedNode();

	public void setStoppedNode( Node xStoppedNode );

	/**
	 * Visszater a parameterkent kapott Node szomszedaival egy Set-ben.
	 * @param xNode Node, aminek a szomszedait akarjuk
	 * @return xNode szomszedos Node-jai
	 */
	public Set< Node >getNeighbours( Node xNode );

	/**
	 * Heurisztikat szamol egy parameterkent adott Node es a vegpont kozott.
	 * @param xNode adott Node
	 * @param xIsForward eloremutato algoritmus-e
	 * @return Node-ra szamolt heurisztika double-kent.
	 */
	public double calculateHeuristic( Node xNode, Boolean xIsForward );

	/**
	 * Kiszamolja ket parameterkent adott Node tavolsagat.
	 * @param xNode1 egyik Node
	 * @param xNode2 masik Node
	 * @return tavolsag a kezdoponttol
	 */
	public double calculateDistance( Node xNode1, Node xNode2 );

	/**
	 * Minden Node allapotat visszaallitja az alapertelmezett ertekere.
	 */
	public void reset();
}
