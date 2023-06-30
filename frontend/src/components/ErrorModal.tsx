import { useEffect, useRef } from "react";
import styles from "../style/ErrorModal.module.css";

type FetchErrorProps = {
  errorCode: string;
  message: string;
  setFetchError: (
    value:
      | {
          errorCode: string;
          message: string;
        }
      | undefined
  ) => void;
};

export const ErrorModal = ({
  errorCode,
  message,
  setFetchError,
}: FetchErrorProps) => {
  const ref = useRef<HTMLDialogElement>(null);

  useEffect(() => {
    ref.current?.showModal();
  }, []);

  const closeModal = () => {
    ref.current?.close();
    setFetchError(undefined);
  };

  return (
    <dialog ref={ref}>
      <p className={styles.title}>결제 실패</p>
      <p>
        {errorCode} : {message}
      </p>
      <p>잠시 후 다시 시도해 주세요.</p>
      <button className={styles.redButton} onClick={() => closeModal()}>
        확인
      </button>
    </dialog>
  );
};
