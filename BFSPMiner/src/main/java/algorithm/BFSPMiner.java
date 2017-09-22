package algorithm;

import model.StatusObject;

/**
 *
 *
 *
 * @author Daniel Toews
 * @version 1.0
 */

public class BFSPMiner {

	private boolean pruning;
	private int delta;
	private PatternBuilder pBuilder;
	private int processedItems;
	private int batchLength;
	private int batch;
	private boolean pruned = true;
	private double alpha, eps;

	/**
	 * The constructor for the BFSPMiner, returns a usable BFSPMiner object
	 *
	 * @param maxPatternLength
	 *            - maximal length of patterns, effects runtime and memory usage
	 * @param pruning
	 *            - true if pruning on. For small data this makes the runtime
	 *            slower. But for bigger datasets and if frequently statusobjects
	 *            are returned (when predicting for example), pruning helps with the
	 *            runtime
	 * @param delta
	 *            - after how many batches do you want to prune? Effects runtime and
	 *            memory usage. If pruning false, this parameter has no effect
	 *
	 * @param batchLength
	 *            - How many items make a batch? Important for the pruning (Details
	 *            about the pruning method can be found in the paper "Stream
	 *            Sequential Pattern Mining with Precise Error Bounds")
	 *
	 * @param alpha
	 *            - pruning threshold, how high has the support at least to be for
	 *            it not to be pruned. Should be close to desired suppThresh
	 *            (Details about the pruning method can be found in the paper
	 *            "Stream Sequential Pattern Mining with Precise Error Bounds")
	 *
	 * @param eps
	 *            - also effects the pruning threshold. Should be between desired
	 *            suppThresh and alpha (Details about the pruning method can be
	 *            found in the paper "Stream Sequential Pattern Mining with Precise
	 *            Error Bounds")
	 *
	 */
	public BFSPMiner(final int maxPatternLength, final boolean pruning, final int delta, final int batchLength,
			final double alpha, final double eps) {
		this.pBuilder = new PatternBuilder(maxPatternLength);
		this.pruning = pruning;
		this.delta = delta;
		this.processedItems = 0;
		this.batchLength = batchLength;
		this.batch = 0;
		this.alpha = alpha;
		this.eps = eps;
	}

	/**
	 * Recieves an event and generates Sequential patterns as described in the paper
	 * BFSPMiner: An Effective and Efficient Batch-Free Algorithm for Mining
	 * Sequential Patterns over Data Streams
	 *
	 * @param eventLabel
	 *            - The newest event in the data stream
	 * @param suppThresh
	 *            - the support threshold for the output
	 * @param output
	 *            - if true, output will be generated, otherwise returns null
	 *            (effects runtime significantly!)
	 * @return
	 */
	public StatusObject createPatterns(final String eventLabel, final double suppThresh, final boolean output) {

		StatusObject status = this.pBuilder.createPatterns(eventLabel, this.processedItems, suppThresh, output,
				this.batch);

		// count how many patterns were generated
		this.processedItems++;
		if (this.processedItems % this.batchLength == 0) {
			this.batch++;
			this.pruned = false;
		}
		if (this.pruning) {
			if (this.batch % this.delta == 0 && !this.pruned) {
				this.pBuilder.ssbePrune(null, this.batch, this.alpha, this.eps, this.processedItems, this.delta);
				this.pruned = true;
			}
		}
		return status;
	}
}
