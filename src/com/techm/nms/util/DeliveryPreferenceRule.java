/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.util;

/**
 * @author sm0015566
 *
 */
public class DeliveryPreferenceRule {

	private String businessTerm;
	
	private String operation;
	
	private Object operandOne;
	
	private Object operandTwo;

	/**
	 * @return the businessTerm
	 */
	public String getBusinessTerm() {
		return businessTerm;
	}

	/**
	 * @param businessTerm the businessTerm to set
	 */
	public void setBusinessTerm(String businessTerm) {
		this.businessTerm = businessTerm;
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return the operandOne
	 */
	public Object getOperandOne() {
		return operandOne;
	}

	/**
	 * @param operandOne the operandOne to set
	 */
	public void setOperandOne(Object operandOne) {
		this.operandOne = operandOne;
	}

	/**
	 * @return the operandTwo
	 */
	public Object getOperandTwo() {
		return operandTwo;
	}

	/**
	 * @param operandTwo the operandTwo to set
	 */
	public void setOperandTwo(Object operandTwo) {
		this.operandTwo = operandTwo;
	}
}
