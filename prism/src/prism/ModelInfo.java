//==============================================================================
//	
//	Copyright (c) 2002-
//	Authors:
//	* Dave Parker <d.a.parker@cs.bham.ac.uk> (University of Birmingham/Oxford)
//	* Nishan Kamaleson <nxk249@bham.ac.uk> (University of Birmingham)
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

import parser.Values;
import parser.VarList;
import parser.ast.Declaration;
import parser.ast.DeclarationBool;
import parser.ast.DeclarationInt;
import parser.ast.Expression;
import parser.ast.RewardStruct;
import parser.type.Type;

/**
 * Interface for classes that provide some basic (syntactic) information about a probabilistic model.
 */
public interface ModelInfo
{
	/**
	 * Get the type of probabilistic model.
	 */
	public ModelType getModelType();

	/**
	 * Set values for *some* undefined constants.
	 * If there are no undefined constants, {@code someValues} can be null.
	 * Undefined constants can be subsequently redefined to different values with the same method.
	 * The current constant values (if set) are available via {@link #getConstantValues()}.
	 */
	public default void setSomeUndefinedConstants(Values someValues) throws PrismException
	{
		// By default, assume there are no constants to define 
		if (someValues != null && someValues.getNumValues() > 0)
			throw new PrismException("This model has no constants to set");
	}

	/**
	 * Get access to the values for all constants in the model, including the 
	 * undefined constants set previously via the method {@link #setUndefinedConstants(Values)}.
	 * Until they are set for the first time, this method returns null.  
	 */
	public default Values getConstantValues()
	{
		// By default, assume there are no constants to define 
		return new Values();
	}

	/**
	 * Does the model contain unbounded variables?
	 */
	public default boolean containsUnboundedVariables()
	{
		// By default, assume all variables are finite-ranging
		return false;
	}

	/**
	 * Get the number of variables in the model. 
	 */
	public default int getNumVars()
	{
		// Default implementation just extracts from getVarNames() 
		return getVarNames().size();
	}
	
	/**
	 * Get the names of all the variables in the model.
	 */
	public List<String> getVarNames();
	
	/**
	 * Look up the index of a variable in the model by name.
	 * Returns -1 if there is no such variable.
	 */
	public default int getVarIndex(String name)
	{
		// Default implementation just extracts from getVarNames() 
		return getVarNames().indexOf(name);
	}

	/**
	 * Get the name of the {@code i}th variable in the model.
	 * {@code i} should always be between 0 and getNumVars() - 1. 
	 */
	public default String getVarName(int i)
	{
		// Default implementation just extracts from getVarNames() 
		return getVarNames().get(i);
	}

	/**
	 * Get the types of all the variables in the model.
	 */
	public List<Type> getVarTypes();

	/**
	 * Get the type of the {@code i}th variable in the model.
	 * {@code i} should always be between 0 and getNumVars() - 1. 
	 */
	public default Type getVarType(int i) throws PrismException
	{
		// Default implementation just extracts from getVarTypes() 
		return getVarTypes().get(i);
	}

	/**
	 * Get the number of labels (atomic propositions) defined for the model. 
	 */
	public default int getNumLabels()
	{
		// Default implementation just extracts from getLabelNames() 
		return getLabelNames().size();
	}
	
	/**
	 * Get the names of all the labels in the model.
	 */
	public default List<String> getLabelNames()
	{
		// No labels by default
		return Collections.emptyList();
	}
	
	/**
	 * Get the name of the {@code i}th label of the model.
	 * {@code i} should always be between 0 and getNumLabels() - 1. 
	 */
	public default String getLabelName(int i) throws PrismException
	{
		// Default implementation just extracts from getLabelNames() 
		try {
			return getLabelNames().get(i);
		} catch (IndexOutOfBoundsException e) {
			throw new PrismException("Label number " + i + " not defined");
		}
	}
	
	/**
	 * Get the index of the label with name {@code name}.
	 * Indexed from 0. Returns -1 if label of that name does not exist.
	 */
	public default int getLabelIndex(String name)
	{
		// Default implementation just extracts from getLabelNames() 
		return getLabelNames().indexOf(name);
	}
	
	// TODO: can we remove this?
	public VarList createVarList() throws PrismException;
}
