package org.isf.medicalstockward.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;
import org.isf.medicalstock.model.Movement;
import org.isf.medicalstockward.model.MedicalWard;
import org.isf.medicalstockward.model.MovementWard;
import org.isf.medicalstockward.service.MedicalStockWardIoOperations;
import org.isf.patient.model.Patient;
import org.isf.serviceprinting.print.MedicalWardForPrint;
import org.isf.serviceprinting.print.MovementForPrint;
import org.isf.serviceprinting.print.MovementWardForPrint;
import org.isf.utils.exception.OHDataValidationException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.isf.ward.model.Ward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovWardBrowserManager {

	private final Logger logger = LoggerFactory.getLogger(MovWardBrowserManager.class);
	
	@Autowired
	private MedicalStockWardIoOperations ioOperations;

    /**
	 * Verify if the object is valid for CRUD and return a list of errors, if any
	 * @param mov
     * @throws OHDataValidationException 
	 */
	protected void validateMovementWard(MovementWard mov) throws OHDataValidationException {
		String description = mov.getDescription();
        List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
        if(description.isEmpty() && mov.isPatient()){
	        errors.add(new OHExceptionMessage("descriptionPatientEmptyError", 
	        		MessageBundle.getMessage("angal.medicalstockwardedit.pleaseselectapatient"), 
	        		OHSeverityLevel.ERROR));
        }
        if(description.isEmpty() && !mov.isPatient()){
	        errors.add(new OHExceptionMessage("descriptionInternalUseEmptyError", 
	        		MessageBundle.getMessage("angal.medicalstockwardedit.pleaseinsertadescriptionfortheinternaluse"), 
	        		OHSeverityLevel.ERROR));
        }
        if (mov.getMedical() == null) {
        	errors.add(new OHExceptionMessage("medicalEmptyError", 
	        		MessageBundle.getMessage("angal.medicalstockwardedit.pleaseselectadrug"), 
	        		OHSeverityLevel.ERROR));
        }
        if (!errors.isEmpty()){
	        throw new OHDataValidationException(errors);
	    }
    }

	/**
	 * Gets all the {@link MovementWard}s.
	 * If an error occurs a message error is shown and the <code>null</code> value is returned.
	 * @return all the retrieved movements ward.
	 * @throws OHServiceException 
	 */
	@Deprecated public ArrayList<MovementWard> getMovementWard() throws OHServiceException {
		return ioOperations.getWardMovements(null, null, null);
	}

	/**
	 * Gets all the {@link MedicalWard}s associated to the specified ward.
	 * @param wardId the ward id.
	 * @return the retrieved medicals.
	 * @throws OHServiceException 
	 */
	public ArrayList<MedicalWard> getMedicalsWard(char wardId) throws OHServiceException {
		return ioOperations.getMedicalsWard(wardId);
	}

	/**
	 * Gets all the movement ward with the specified criteria.
	 * @param wardId the ward id.
	 * @param dateFrom the lower bound for the movement date range.
	 * @param dateTo the upper bound for the movement date range.
	 * @return all the retrieved movements.
	 * @throws OHServiceException 
	 */
	public ArrayList<MovementWard> getMovementWard(String wardId, GregorianCalendar dateFrom, GregorianCalendar dateTo) throws OHServiceException {
		return ioOperations.getWardMovements(wardId, dateFrom, dateTo);
	}
        
     /**
	 * Gets all the movement ward with the specified criteria.
	 * @param idwardTo the target ward id.
	 * @param dateFrom the lower bound for the movement date range.
	 * @param dateTo the upper bound for the movement date range.
	 * @return all the retrieved movements.
	 * @throws OHServiceException 
	 */
	public ArrayList<MovementWard> getWardMovementsToWard(String idwardTo, GregorianCalendar dateFrom, GregorianCalendar dateTo) throws OHServiceException {
		return ioOperations.getWardMovementsToWard(idwardTo, dateFrom, dateTo);
	}
	
    /**
	 * Gets all the movement ward with the specified criteria.
	 * @param patId.

	 * @return all the retrieved movements.
	 * @throws OHServiceException 
	 */
	public ArrayList<MovementWard> getMovementToPatient(Patient patient) throws OHServiceException {
		return ioOperations.getWardMovementsToPatient(patient.getCode());
	}
	
	/**
	 * Persists the specified movement.
	 * @param newMovement the movement to persist.
	 * @return <code>true</code> if the movement has been persisted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newMovementWard(MovementWard newMovement) throws OHServiceException {
		validateMovementWard(newMovement);
		return ioOperations.newMovementWard(newMovement);
	}

	/**
	 * Persists the specified movements.
	 * @param newMovements the movements to persist.
	 * @return <code>true</code> if the movements have been persisted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newMovementWard(ArrayList<MovementWard> newMovements) throws OHServiceException {
		List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
		if (newMovements.size() == 0) {
			errors.add(new OHExceptionMessage(
					"emptyMovementListError", 
					MessageBundle.getMessage("angal.medicalstockwardedit.pleaseselectadrug"), 
					OHSeverityLevel.ERROR));
			throw new OHDataValidationException(errors);
		}
		for (MovementWard mov : newMovements) {
			validateMovementWard(mov);
		}
		return ioOperations.newMovementWard(newMovements);
	}
    
	/**
	 * Updates the specified {@link MovementWard}.
	 * @param updateMovement the movement ward to update.
	 * @return <code>true</code> if the movement has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateMovementWard(MovementWard updateMovement) throws OHServiceException {
		return ioOperations.updateMovementWard(updateMovement);
	}

	/**
	 * Deletes the specified {@link MovementWard}.
	 * @param movement the movement to delete.
	 * @return <code>true</code> if the movement has been deleted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	protected boolean deleteMovementWard(MovementWard movement) throws OHServiceException {
		return ioOperations.deleteMovementWard(movement);
	}

	/**
	 * Gets the current quantity for the specified {@link Medical} and specified {@link Ward}.
	 * @param ward - if {@code null} the quantity is counted for the whole hospital
	 * @param medical - the {@link Medical} to check.
	 * @return the total quantity.
	 * @throws OHServiceException if an error occurs retrieving the quantity.
	 */
	public int getCurrentQuantityInWard(Ward ward, Medical medical) throws OHServiceException {
		return ioOperations.getCurrentQuantityInWard(ward, medical);
	}

	public ArrayList<MovementWardForPrint> convertMovementWardForPrint(ArrayList<MovementWard> wardOutcomes) {
		ArrayList<MovementWardForPrint> movPrint = new ArrayList<MovementWardForPrint>();
		for (MovementWard mov : wardOutcomes) {
			movPrint.add(new MovementWardForPrint(mov));
		}
		Collections.sort(movPrint, new ComparatorMovementWardForPrint());
		return movPrint;
	}

	public ArrayList<MovementForPrint> convertMovementForPrint(ArrayList<Movement> wardIncomes) {
		ArrayList<MovementForPrint> movPrint = new ArrayList<MovementForPrint>();
		for (Movement mov : wardIncomes) {
			movPrint.add(new MovementForPrint(mov));
		}
		Collections.sort(movPrint, new ComparatorMovementForPrint());
		return movPrint;
	}

	public ArrayList<MedicalWardForPrint> convertWardDrugs(Ward wardSelected, ArrayList<MedicalWard> wardDrugs) {
		ArrayList<MedicalWardForPrint> drugPrint = new ArrayList<MedicalWardForPrint>();
		for (MedicalWard mov : wardDrugs) {
			drugPrint.add(new MedicalWardForPrint(mov, wardSelected));
		}
		Collections.sort(drugPrint);
		return drugPrint;
	}
	
	class ComparatorMovementWardForPrint implements Comparator<MovementWardForPrint>  {
		@Override
		public int compare(MovementWardForPrint o1, MovementWardForPrint o2) {
			int byDate = o2.getDate().compareTo(o1.getDate());
			if (byDate != 0) {
				return byDate;
			} else {
				return o1.getMedical().compareTo(o2.getMedical());
			}
		}
	}
	
	class ComparatorMovementForPrint implements Comparator<MovementForPrint>  {
		@Override
		public int compare(MovementForPrint o1, MovementForPrint o2) {
			int byDate = o2.getDate().compareTo(o1.getDate());
			if (byDate != 0) {
				return byDate;
			} else {
				return o1.getMedical().compareTo(o2.getMedical());
			}
		}
	}
}
