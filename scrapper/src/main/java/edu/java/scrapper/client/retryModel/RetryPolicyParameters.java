package edu.java.scrapper.client.retryModel;

import java.util.Set;
import org.springframework.http.HttpStatus;

public class RetryPolicyParameters {
    private CustomRetryPolicy policy;
    private int number;
    private Set<HttpStatus> statuses;

    public RetryPolicyParameters() {
    }

    public void setPolicy(CustomRetryPolicy policy) {
        this.policy = policy;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setStatuses(Set<HttpStatus> statuses) {
        this.statuses = statuses;
    }

    public CustomRetryPolicy getPolicy() {
        return policy;
    }

    public int getNumber() {
        return number;
    }

    public Set<HttpStatus> getStatuses() {
        return statuses;
    }
}
