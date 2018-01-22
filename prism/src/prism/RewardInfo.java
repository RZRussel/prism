//==============================================================================
//	
//	Copyright (c) 2018-
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

import java.util.Collections;
import java.util.List;

/**
 * Interface for classes that provide some basic (syntactic) information about rewards for a model.
 */
public interface RewardInfo
{
	/**
	 * Get the number of reward structures.
	 */
	public default int getNumRewardStructs()
	{
		// Default implementation just extracts from getRewardStructNames() 
		return getRewardStructNames().size();
	}
	
	/**
	 * Get a list of the names of the reward structures.
	 */
	public default List<String> getRewardStructNames()
	{
		// No reward structures by default
		return Collections.emptyList();
	}
}
