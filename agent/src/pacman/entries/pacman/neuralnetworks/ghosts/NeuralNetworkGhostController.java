package pacman.entries.pacman.neuralnetworks.ghosts;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import pacman.controllers.Controller;
import pacman.entries.pacman.GameTask;
import pacman.entries.pacman.neuralnetworks.moveselectionstrategies.MoveSelectionStrategy;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class NeuralNetworkGhostController extends Controller<EnumMap<GHOST, MOVE>> implements GameTask
{
	private Map<GHOST, GhostNeuralNetwork> networks;
	private int iterations;


	public NeuralNetworkGhostController(MoveSelectionStrategy selectionStrategy, int iterations, boolean useLearnedWeights, boolean bulkTrain)
	{
		this.iterations = iterations;
		
		networks = new HashMap<GHOST, GhostNeuralNetwork>();

        if (useLearnedWeights)
        {
            networks.put(GHOST.BLINKY, new GhostNeuralNetwork(GHOST.BLINKY, selectionStrategy,
                    LearnedWeights.getBlinkyTheta1(), LearnedWeights.getBlinkyTheta2(), bulkTrain));

            networks.put(GHOST.PINKY, new GhostNeuralNetwork(GHOST.PINKY, selectionStrategy,
                    LearnedWeights.getPinkyTheta1(), LearnedWeights.getPinkyTheta2(), bulkTrain));

            networks.put(GHOST.INKY, new GhostNeuralNetwork(GHOST.INKY, selectionStrategy,
                    LearnedWeights.getInkyTheta1(), LearnedWeights.getInkyTheta2(), bulkTrain));

            networks.put(GHOST.SUE, new GhostNeuralNetwork(GHOST.SUE, selectionStrategy,
                    LearnedWeights.getSueTheta1(), LearnedWeights.getSueTheta2(), bulkTrain));
        }
        else
        {
            networks.put(GHOST.BLINKY, new GhostNeuralNetwork(GHOST.BLINKY, selectionStrategy, bulkTrain));
            networks.put(GHOST.PINKY, new GhostNeuralNetwork(GHOST.PINKY, selectionStrategy, bulkTrain));
            networks.put(GHOST.INKY, new GhostNeuralNetwork(GHOST.INKY, selectionStrategy, bulkTrain));
            networks.put(GHOST.SUE, new GhostNeuralNetwork(GHOST.SUE, selectionStrategy, bulkTrain));
        }
	}


    public NeuralNetworkGhostController(MoveSelectionStrategy selectionStrategy, int iterations, boolean useLearnedWeights)
    {
        this(selectionStrategy, iterations, useLearnedWeights, false);
    }


    public NeuralNetworkGhostController(MoveSelectionStrategy selectionStrategy, int iterations)
    {
        this(selectionStrategy, iterations, true);
    }


	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timedue)
	{
		EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
		
		for (GHOST ghost: GHOST.values())
		{
			if (game.doesGhostRequireAction(ghost))
				moves.put(ghost, networks.get(ghost).getMove(game));
		}
		
		return moves;
	}
	
	
	@Override
	public void run(Game game)
	{
		for (GHOST ghost: GHOST.values())
		{
			networks.get(ghost).train(game, iterations);
		}
	}
}
