import { useEffect, useState } from "react";
import { ReceiptData } from "../App";
import styles from "../style/Receipt.module.css";

export const Receipt = ({
  receiptData,
  clearState,
}: {
  receiptData: ReceiptData;
  clearState: () => void;
}) => {
  const second = 10;
  const [timer, setTimer] = useState(second);

  useEffect(() => {
    const timerInterval = setInterval(
      () =>
        setTimer((prev) => {
          return prev - 1;
        }),
      1000
    );

    return () => {
      clearInterval(timerInterval);
    };
  }, []);

  useEffect(() => {
    if (timer <= 0) {
      clearState();
    }
  }, [timer, clearState]);

  return (
    <div className={styles.receipt}>
      <div className={styles.orderId}>주문 번호 {receiptData.id}</div>

      <div className={styles.receiptContent}>
        <table className={styles.receiptTable}>
          <thead>
            <tr>
              <th className={styles.menu}>메뉴와 옵션</th>
              <th className={styles.unitPrice}>단가</th>
              <th className={styles.quantity}>수량</th>
              <th className={styles.amount}>금액</th>
            </tr>
          </thead>
          <tbody>
            {receiptData.items.map((item, index) => (
              <tr className={styles.contentItem} key={index}>
                <td className={styles.menu}>
                  <div>{item.name}</div>
                  <div className={styles.options}>
                    {item.options
                      .map((optionObject) =>
                        Object.values(optionObject)
                          .filter((value) => value)
                          .map((value) => value?.name)
                          .join(" / ")
                      )
                      .join(" / ")}
                  </div>
                </td>
                <td className={styles.unitPrice}>
                  {item.price / item.quantity}
                </td>
                <td className={styles.quantity}>{item.quantity}</td>
                <td className={styles.amount}>{item.price}원</td>
              </tr>
            ))}
            <tr>
              <th className={styles.menu}>결제 방식</th>
              <td></td>
              <td></td>
              <td className={styles.amount}>{receiptData.payments}</td>
            </tr>
            {receiptData.payments === "현금결제" && (
              <tr>
                <th className={styles.menu}>투입 금액</th>
                <td></td>
                <td></td>
                <td className={styles.amount}>{receiptData.amount}원</td>
              </tr>
            )}
            <tr>
              <th className={styles.menu}>총 결제 금액</th>
              <td></td>
              <td></td>
              <td className={styles.amount}>{receiptData.total}원</td>
            </tr>
            {receiptData.payments === "현금결제" && (
              <tr>
                <th className={styles.menu}>잔돈</th>
                <td></td>
                <td></td>
                <td className={styles.amount}>{receiptData.remain}원</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
      <p className={styles.caution}>
        (주의 : 이 화면은 {timer}초뒤에 자동으로 사라집니다)
      </p>
    </div>
  );
};
