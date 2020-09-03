package exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FailedLoadResultException extends Exception {

    public FailedLoadResultException(String message) {
        super(message);
    }
}
