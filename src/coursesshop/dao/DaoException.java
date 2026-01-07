package coursesshop.dao;

/**
 * Runtime exception used to wrap low-level persistence/JDBC errors in the DAO layer.
 *
 * <p>This exception provides a single, consistent unchecked exception type for database-related
 * failures (e.g., {@code SQLException}, configuration issues, mapping errors) so that callers
 * in the business/UI layers do not need to handle JDBC-specific exceptions directly.
 * </p>
 */
public class DaoException extends RuntimeException {

  /**
   * Creates a new {@link DaoException} with a message and an underlying cause.
   *
   * @param message a human-readable explanation of the error
   * @param cause   the root cause (typically a {@link java.sql.SQLException} or
   *{@link java.io.IOException})
   */
  public DaoException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new {@link DaoException} with a message only.
   *
   * @param message a human-readable explanation of the error
   */
  public DaoException(String message) {
    super(message);
  }
}
