package org.app4mc.cdgen.tool.java.utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.app4mc.cdgen.tool.java.identifiers.NumericConstants;
import org.app4mc.cdgen.tool.java.utils_amalthea.ConstraintsUtil;
import org.app4mc.cdgen.tool.java.utils_amalthea.FactoryUtil;
import org.app4mc.cdgen.tool.java.utils_amalthea.HardwareUtil;
import org.app4mc.cdgen.tool.java.utils_amalthea.SoftwareUtil;
import org.app4mc.cdgen.tool.java.utils_amalthea.TimeUtil;
import org.app4mc.cdgen.tool.java.utils_amalthea.RuntimeUtil.AccessDirection;
import org.app4mc.cdgen.tool.java.utils_amalthea.RuntimeUtil.TimeType;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.HwAccessElement;
import org.eclipse.app4mc.amalthea.model.HwDestination;
import org.eclipse.app4mc.amalthea.model.IDiscreteValueDeviation;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.ProcessingUnitDefinition;
import org.eclipse.app4mc.amalthea.model.PuType;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.emf.common.util.EList;
import org.eclipse.app4mc.amalthea.model.Runnable;

/**
 * Implementation to Support E2E file - End to End Chain manipulation and computation.
 * 
 * @author RockNRollMan
 *
 */
public class E2EUtils{
	/**
	 * Gets the Access Time taken by the task in the given direction.
	 *
	 * @param t Task
	 * @param pu ProcessingUnit
	 * @param accdir AccessDirection
	 * @return Time
	 */
	public static Time taskAccessTime(final Task t, final ProcessingUnit pu, AccessDirection accdir) {
		Time totalAccessTime = FactoryUtil.createTime();
		long totalTimeValueUnscaled = 0;
		long freqScalar = 1000000000;
		final int cacheLine = 64;
		Set<Label> labelSet = new HashSet<Label>();
		EList<HwAccessElement> memoryAccessElement = pu.getAccessElements();
		IDiscreteValueDeviation memoryLatencyInCyclesDev = null;
		if (accdir.equals(AccessDirection.READ)) {
			labelSet = SoftwareUtil.getReadLabelSet(t, null);
			memoryLatencyInCyclesDev = memoryAccessElement.get(0).getReadLatency();
		}else if (accdir.equals(AccessDirection.WRITE)) {
			labelSet = SoftwareUtil.getWriteLabelSet(t, null);
			memoryLatencyInCyclesDev = memoryAccessElement.get(0).getWriteLatency();
		}
		Long memoryReadLatencyInCycles = memoryLatencyInCyclesDev.getUpperBound();//worst case latency is assumed
		long coreFreqInMHz = HardwareUtil.getFrequencyOfModuleInHz(pu)/freqScalar;
		
		for (final Label label: labelSet ) {
			long sizeInBytes = label.getSize().getNumberBytes();
			long cacheLineCount = sizeInBytes/cacheLine;
			double readLatencyTime = (double) memoryReadLatencyInCycles/(double)coreFreqInMHz;
			long localTimeValueInUS = (long) (readLatencyTime*cacheLineCount);
			totalTimeValueUnscaled = totalTimeValueUnscaled + localTimeValueInUS;
		}
		totalAccessTime.setValue(BigInteger.valueOf(totalTimeValueUnscaled));
		totalAccessTime.setUnit(TimeUnit.NS);
		totalAccessTime.adjustUnit();
		return totalAccessTime;
	}
	
	
	
	public static Time taskAccessTime(final Runnable r, final ProcessingUnit pu, AccessDirection accdir) {
		Time totalAccessTime = FactoryUtil.createTime();
		long totalTimeValueUnscaled = 0;
		long freqScalar = 1000000000;
		final int cacheLine = 64;
		Set<Label> labelSet = new HashSet<Label>();
		EList<HwAccessElement> memoryAccessElement = pu.getAccessElements();
		IDiscreteValueDeviation memoryLatencyInCyclesDev = null;
		if (accdir.equals(AccessDirection.READ)) {
			labelSet = SoftwareUtil.getReadLabelSet(r, null);
			memoryLatencyInCyclesDev = memoryAccessElement.get(0).getReadLatency();
		}else if (accdir.equals(AccessDirection.WRITE)) {
			labelSet = SoftwareUtil.getWriteLabelSet(r, null);
			memoryLatencyInCyclesDev = memoryAccessElement.get(0).getWriteLatency();
		}
		Long memoryReadLatencyInCycles = memoryLatencyInCyclesDev.getUpperBound();//worst case latency is assumed
		long coreFreqInMHz = HardwareUtil.getFrequencyOfModuleInHz(pu)/freqScalar;
		for (final Label label: labelSet ) {
			long sizeInBytes = label.getSize().getNumberBytes();
			long cacheLineCount = sizeInBytes/cacheLine;
			double readLatencyTime = (double) memoryReadLatencyInCycles/(double)coreFreqInMHz;
			long localTimeValueInUS = (long) (readLatencyTime*cacheLineCount);
			totalTimeValueUnscaled = totalTimeValueUnscaled + localTimeValueInUS;
		}
		totalAccessTime.setValue(BigInteger.valueOf(totalTimeValueUnscaled));
		totalAccessTime.setUnit(TimeUnit.NS);
		totalAccessTime.adjustUnit();
		return totalAccessTime;
		
	}
	
	
	
	
	
	
	
	
	/**
	 * Gets the Time taken at the memory contention points by the task.
	 *
	 * @param task Task
	 * @param pu ProcessingUnit
	 * @return Time
	 */
	public static Time getMemoryContentionCost(final Task task, final ProcessingUnit pu) {
		int worstCaseAccessLatency = 0;
		ProcessingUnitDefinition puDef = pu.getDefinition();
		if (puDef.getName().equals("A57")){
			worstCaseAccessLatency = NumericConstants.WCAL_A57_NS;
		}else if (puDef.getName().equals("Denver")) {
			worstCaseAccessLatency = NumericConstants.WCAL_DENVER_NS;
		}else {
			worstCaseAccessLatency = NumericConstants.WCAL_GPU_NS;
		}
		Time Ap = FactoryUtil.createTime(worstCaseAccessLatency, TimeUnit.NS);
		Set<Label> labelSet = SoftwareUtil.getWriteLabelSet(task, null);
		double accessMultiplier = 0.0;
		for (Label label: labelSet) {
			double cachLineCount = label.getSize().getNumberBytes()/NumericConstants.CACHE_LINE_SIZE_BYTES;
			accessMultiplier = accessMultiplier + cachLineCount;
		}
		Time memContentionPenalty = Ap.multiply(accessMultiplier);
		return memContentionPenalty;
	}
	
	/**
	 * Gets the Time taken at the memory contention points by the task in the GPU.
	 *
	 * @param task Task
	 * @param pu ProcessingUnit
	 * @return Time
	 */
	public static Time getMemoryContentionCostGPU(final Task task, final ProcessingUnit pu) {
		int worstCaseAccessLatency = 0;
		ProcessingUnitDefinition puDef = pu.getDefinition();
		if (!puDef.getName().equals("GPU_def")) {
			System.out.println("error");
		}
		Time Ap = FactoryUtil.createTime(worstCaseAccessLatency, TimeUnit.NS);
		Set<Label> labelSet = SoftwareUtil.getWriteLabelSet(task, null);
		double accessMultiplier = 0.0;
		for (Label label: labelSet) {
			double cachLineCount = label.getSize().getNumberBytes()/NumericConstants.CACHE_LINE_SIZE_BYTES;
			accessMultiplier = accessMultiplier + cachLineCount;
		}
		Time memContentionPenalty = Ap.multiply(accessMultiplier);
		return memContentionPenalty;
	}
	
	/**
	 * Gets the LCM for the provided array of Periods.
	 * Reference - //http://programmertech.com/program/java/java-program-find-lcm-of-numbers
	 * @param periodArray array of Periods
	 * @return long
	 */
	public static long lcm(long[] periodArray) {
        long lcm = 1;
        int divisor = 2;
        while (true) {
            int cnt = 0;
            boolean divisible = false;
            for (int i = 0; i < periodArray.length; i++) {
                if (periodArray[i] == 0) {
                    return 0;
                } else if (periodArray[i] < 0) {
                    periodArray[i] = periodArray[i] * (-1);
                }
                if (periodArray[i] == 1) {
                    cnt++;
                }
                if (periodArray[i] % divisor == 0) {
                    divisible = true;
                    periodArray[i] = periodArray[i] / divisor;
                }
            }
            if (divisible) {
                lcm = lcm * divisor;
            } else {
                divisor++;
            }
            if (cnt == periodArray.length) {
                return lcm;
            }
        }
    }
	/**
	 * Gets the LCM for the provided 2 values.
	 * @param num1 value 1
	 * @param num2 value 2
	 * @return int
	 */
    public static int lcm2(int num1, int num2) {
        if(num1==0 || num2==0){
            return 0;
        }else if(num1<0){
            num1=num1*(-1);
        }else if(num2<0){
            num2=num2*(-1);
        }
        int m = num1;
        int n = num2;
        while (num1 != num2) {
            if (num1 < num2) {
                num1 = num1 + m;
            } else {
                num2 = num2 + n;
            }
        }
        return num1;
    }
}