package net.samhouse.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.samhouse.Utils.timeToString;

/**
 * Order POJO
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 5887207333319968742L;

    /**
     * represents current order id, should be a uuid value
     */
    private String orderID;

    /**
     * order's start processing time
     */
    private long startTime;

    /**
     * order's completed processing time
     */
    private long completeTime;

    /**
     * order's currentStep, please refer to Step definition for more detail
     */
    private Step currentStep;

    /**
     * Used for represent order items
     */
    private String payLoad;

    /**
     * array list to store steps completed, generally,
     * there won't be more than 5 steps changing an order from sheduling to completed
     * so, set the initial capacity to 6
     */
    private List<Step> steps = new ArrayList<>(6);

    /**
     * default constructor
     */
    public Order() {
    }

    /**
     * @return
     */
    public Order init() {
        this.orderID = UUID.randomUUID().toString();
        this.startTime = System.currentTimeMillis();
        this.completeTime = this.startTime;
        this.currentStep = new Step(this.startTime);
        this.payLoad = "";

        return this;
    }

    /**
     * @param orderID
     * @param startTime
     * @param completeTime
     * @param currentStep
     */
    public Order(String orderID, long startTime, long completeTime, Step currentStep, String payLoad) {
        this.orderID = orderID;
        this.startTime = startTime;
        this.completeTime = completeTime;
        this.currentStep = currentStep;
        this.payLoad = payLoad;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(long completeTime) {
        this.completeTime = completeTime;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public Step getCurrentStep() {
        return this.currentStep;
    }

    public void setCurrentStep(Step currentStep) {
        this.currentStep = currentStep;
    }

    public String getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }

    /**
     * @param currentPhase
     */
    private void changeCurrentStep(Step.Phase currentPhase) {
        if (currentStep.getCurrentPhase() != currentPhase) {
            steps.add(currentStep.changeCurrentPhase(currentPhase));
        }
    }

    /**
     * as step phase changed, we need to add an item to step list
     *
     * @param currentStep
     */
    public void changeCurrentStep(Step currentStep) {
        changeCurrentStep(currentStep.getCurrentPhase());
    }

    /**
     * set the currentStep to failed directly and add an item to step list
     */
    public void setToFailed() {
        if (currentStep.getCurrentPhase() != Step.Phase.FAILED) {
            changeCurrentStep(Step.Phase.FAILED);
        }
        setCompleteTime(System.currentTimeMillis());
    }

    /**
     * Move to the next currentStep, if current currentStep is completed, then
     * next currentStep will be set to default failed
     */
    public void moveToNextStep() {
        Step.Phase nextPhase = currentStep.GetNextPhase();
        changeCurrentStep(nextPhase);
        if (currentStep.getCurrentPhase().equals(Step.Phase.COMPLETED) ||
                currentStep.getCurrentPhase().equals(Step.Phase.FAILED)) {
            setCompleteTime(System.currentTimeMillis());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (startTime != order.startTime) return false;
        if (completeTime != order.completeTime) return false;
        if (orderID != null ? !orderID.equals(order.orderID) : order.orderID != null) return false;
        if (currentStep != null ? !currentStep.equals(order.currentStep) : order.currentStep != null) return false;
        if (payLoad != null ? !payLoad.equals(order.payLoad) : order.payLoad != null) return false;
        return steps != null ? steps.equals(order.steps) : order.steps == null;
    }

    @Override
    public int hashCode() {
        int result = orderID != null ? orderID.hashCode() : 0;
        result = 31 * result + (int) (startTime ^ (startTime >>> 32));
        result = 31 * result + (int) (completeTime ^ (completeTime >>> 32));
        result = 31 * result + (currentStep != null ? currentStep.hashCode() : 0);
        result = 31 * result + (payLoad != null ? payLoad.hashCode() : 0);
        result = 31 * result + (steps != null ? steps.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("orderID='").append(orderID).append('\'');
        sb.append(", startTime=").append(timeToString(startTime));
        sb.append(", completeTime=").append(timeToString(completeTime));
        sb.append(", currentStep=").append(currentStep);
        sb.append(", payLoad='").append(payLoad).append('\'');
        sb.append(", steps=").append(steps);
        sb.append('}');
        return sb.toString();
    }
}
