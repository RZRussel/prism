//==============================================================================
//	
//	Copyright (c) 2002-
//	Authors:
//	* Dave Parker <d.a.parker@cs.bham.ac.uk> (University of Birmingham)
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

package prism;

import parser.State;

/**
 * Interface for classes that generate rewards for a model.
 */
public interface RewardGenerator extends RewardInfo
{
	/** Mechanisms for querying rewards */
	public enum RewardLookup { BY_STATE, BY_STATE_INDEX };
	
	/**
	 * Returns the mechanism by which rewards are queried.
	 */
	public default RewardLookup getRewardLookup()
	{
		// By default, rewards are queried via State objects
		return RewardLookup.BY_STATE;
	}
	
	/**
	 * Returns true if the {@code r}th reward structure defines state rewards.
	 * ({@code r} is indexed from 0, not from 1 like at the user (property language) level).
	 * If this returns false, the model checker is allowed to ignore them (which may be more efficient).
	 * If using an algorithm or implementation that does not support state rewards,
	 * you may need to return false here (as well as not defining state rewards).
	 */
	public default boolean rewardStructHasStateRewards(int r)
	{
		// By default, assume that any reward structures that do exist may have state rewards
		return true;
	}
	
	/**
	 * Returns true if the {@code r}th reward structure defines transition rewards.
	 * ({@code r} is indexed from 0, not from 1 like at the user (property language) level).
	 * If this returns false, the model checker is allowed to ignore them (which may be more efficient).
	 * If using an algorithm or implementation that does not support transition rewards,
	 * you may need to return false here (as well as not defining transition rewards).
	 */
	public default boolean rewardStructHasTransitionRewards(int r)
	{
		// By default, assume that any reward structures that do exist may have transition rewards
		return true;
	}
	
	/**
	 * Get the state reward of the {@code r}th reward structure for state {@code state}
	 * ({@code r} is indexed from 0, not from 1 like at the user (property language) level).
	 * @param r The index of the reward structure to use
	 * @param state The state in which to evaluate the rewards
	 */
	public default double getStateReward(int r, State state) throws PrismException
	{
		// Default reward to 0 (no reward structures by default anyway)
		return 0.0;
	}

	
	/**
	 * Get the state-action reward of the {@code r}th reward structure for state {@code state} and action {@code action}
	 * ({@code r} is indexed from 0, not from 1 like at the user (property language) level).
	 * If a reward structure has no transition rewards, you can indicate this by implementing
	 * the method {@link #rewardStructHasTransitionRewards(int)}, which may improve efficiency
	 * and/or allow use of algorithms/implementations that do not support transition rewards rewards.
	 * @param r The index of the reward structure to use
	 * @param state The state in which to evaluate the rewards
	 * @param action The outgoing action label
	 */
	public default double getStateActionReward(int r, State state, Object action) throws PrismException
	{
		// Default reward to 0 (no reward structures by default anyway)
		return 0.0;
	}
	
	/**
	 * Get the state reward of the {@code r}th reward structure for state {@code s}
	 * ({@code r} is indexed from 0, not from 1 like at the user (property language) level).
	 * If a reward structure has no state rewards, you can indicate this by implementing
	 * the method {@link #rewardStructHasStateRewards(int)}, which may improve efficiency
	 * and/or allow use of algorithms/implementations that do not support state rewards rewards.
	 * @param r The index of the reward structure to use
	 * @param s The index of the state in which to evaluate the rewards
	 */
	public default double getStateReward(int r, int s) throws PrismException
	{
		// Default reward to 0 (no reward structures by default anyway)
		return 0.0;
	}

	
	/**
	 * Get the state-action reward of the {@code r}th reward structure for state {@code s} and action {@code action}
	 * ({@code r} is indexed from 0, not from 1 like at the user (property language) level).
	 * If a reward structure has no transition rewards, you can indicate this by implementing
	 * the method {@link #hasTransitionRewards(int)}, which may improve efficiency
	 * and/or allow use of algorithms/implementations that do not support transition rewards rewards.
	 * @param r The index of the reward structure to use
	 * @param s The index of the state in which to evaluate the rewards
	 * @param action The outgoing action label
	 */
	public default double getStateActionReward(int r, int s, Object action) throws PrismException
	{
		// Default reward to 0 (no reward structures by default anyway)
		return 0.0;
	}
}
