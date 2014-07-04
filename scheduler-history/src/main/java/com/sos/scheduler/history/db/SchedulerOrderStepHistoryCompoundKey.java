package com.sos.scheduler.history.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

 
/**
* \class SchedulerOrderStepHistoryCompoundKey 
* 
* \brief SchedulerOrderStepHistoryCompoundKey - 
* 
* \details
*
* \section SchedulerOrderStepHistoryCompoundKey.java_intro_sec Introduction
*
* \section SchedulerOrderStepHistoryCompoundKey.java_samples Some Samples
*
* \code
*   .... code goes here ...
* \endcode
*
* <p style="text-align:center">
* <br />---------------------------------------------------------------------------
* <br /> APL/Software GmbH - Berlin
* <br />##### generated by ClaviusXPress (http://www.sos-berlin.com) #########
* <br />---------------------------------------------------------------------------
* </p>
* \author Uwe Risse
* \version 18.10.2011
* \see reference
*
* Created on 18.10.2011 10:43:49
 */

@Embeddable
public class SchedulerOrderStepHistoryCompoundKey implements Serializable {
		 /**
		 * 
		 */
		private static final long	serialVersionUID	= 1L;
		 private Long historyId;
	     private Long step;

	     public SchedulerOrderStepHistoryCompoundKey() {
	    	  }
	     
	     public SchedulerOrderStepHistoryCompoundKey(Long historyId, Long step) {
	    	    this.historyId = historyId;
	    	    this.step =step;
	    	  }
	   
	     @Column(name="`HISTORY_ID`",nullable=false) 
	     public Long getHistoryId() {
	    	 return historyId;
	     }
	     
	     @Column(name="`HISTORY_ID`",nullable=false) 
	     public void setHistoryId(Long historyId) {
	       this.historyId = historyId;
	     }
	     
	     @Column(name="`STEP`",nullable=false)
	     public Long getStep() {
	    	 return step;
	     }
	     
	     @Column(name="`STEP`",nullable=false)
	     public void setStep(Long step) {
	       this.step = step;
	     }
	     
	     @Transient
	     public String getStepValue() {
	         return String.valueOf(step);
	     }
	     
	     public boolean equals(Object key) {
	    	   boolean result = true;
	    	   if (!(key instanceof SchedulerOrderStepHistoryCompoundKey)) {return false;}
	    	    Long otherHistoryId = ((SchedulerOrderStepHistoryCompoundKey)key).getHistoryId();
	    	    Long otherStep = ((SchedulerOrderStepHistoryCompoundKey)key).getStep();
	    	    if (step == null || otherStep == null) {
	    	      result = false;
	    	    }else {
	    	      result = step.equals(otherStep);
	    	    }
	    	    if (historyId == null || otherHistoryId == null) {
	    	      result = false;
	    	    }else {
	    	      result = historyId.equals(otherHistoryId);
	    	    }
	    	   return result;
	    	  }

	    	  public int hashCode() {
	    	    int code = 0;
	    	    if (step!=null) {code +=step;}
	    	    if (historyId!=null) {code +=historyId;}
	    	    return code;
	    	  }
	 }