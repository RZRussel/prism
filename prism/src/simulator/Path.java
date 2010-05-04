//==============================================================================
//	
//	Copyright (c) 2002-
//	Authors:
//	* Dave Parker <david.parker@comlab.ox.ac.uk> (University of Oxford)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of PRISM.
//	
//	PRISM is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//	
//	PRISM is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//	
//	You should have received a copy of the GNU General Public License
//	along with PRISM; if not, write to the Free Software Foundation,
//	Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//	
//==============================================================================

package simulator;

import parser.State;

/**
 * Classes that store and manipulate a path though a model.
 */
public abstract class Path
{
	// MUTATORS
	
	/**
	 * Initialise the path with an initial state and rewards.
	 */
	public abstract void initialise(State initialState, double[] initialStateRewards);

	/**
	 * Add a step to the path.
	 */
	public abstract void addStep(int choice, String action, double[] transRewards, State newState, double[] newStateRewards);

	/**
	 * Add a timed step to the path.
	 */
	public abstract void addStep(double time, int choice, String action, double[] transRewards, State newState, double[] newStateRewards);

	// ACCESSORS

	/**
	 * Get the size of the path (number of steps; or number of states - 1).
	 */
	public abstract int size();

	/**
	 * Get the previous state, i.e. the penultimate state of the current path.
	 */
	public abstract State getPreviousState();

	/**
	 * Get the current state, i.e. the current final state of the path.
	 */
	public abstract State getCurrentState();

	/**
	 * For paths with continuous-time info, get the total time elapsed so far
	 * (where zero time has been spent in the current (final) state).
	 */
	public abstract double getTimeSoFar();
	
	/**
	 * For paths with continuous-time info, get the time spent in the previous state.
	 */
	public abstract double getTimeInPreviousState();
	
	/**
	 * Get the total reward accumulated so far
	 * (includes reward for previous transition but no state reward for current (final) state).
	 * @param index Reward structure index
	 */
	public abstract double getRewardCumulatedSoFar(int index);
	
	/**
	 * Get the state reward for the previous state.
	 * (For continuous-time models, need to multiply this by time spent in the state.)
	 * @param index Reward structure index
	 */
	public abstract double getPreviousStateReward(int index);
	
	/**
	 * Get the transition reward for the transition between the previous and current states.
	 * @param index Reward structure index
	 */
	public abstract double getPreviousTransitionReward(int index);

	/**
	 * Get the state reward for the current state.
	 * (For continuous-time models, need to multiply this by time spent in the state.)
	 * @param index Reward structure index
	 */
	public abstract double getCurrentStateReward(int index);
}
