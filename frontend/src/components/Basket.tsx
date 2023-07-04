import { useEffect, useRef, useState } from "react";
import styles from "../style/Basket.module.css";
import { Indicator, PaymentModal } from "./PaymentModal";
import { ReceiptData } from "../App";
type OptionValue = {
  id: number;
  name: string;
};

type Options = {
  [key: string]: OptionValue;
};

export type basketList = {
  id: string;
  name: string;
  price: number;
  count: number;
  image: string;
  options: Options;
};

interface BasketProps {
  basketList: basketList[];
  setBasketList: React.Dispatch<React.SetStateAction<any[]>>;
  setReceiptData: React.Dispatch<React.SetStateAction<ReceiptData | undefined>>;
}

export const Basket = ({
  basketList,
  setBasketList,
  setReceiptData,
}: BasketProps) => {
  const second = 30;
  const [timer, setTimer] = useState(second);
  const [isModalOpen, setIsModalOpen] = useState<Boolean>(false);
  const [isPaying, setIsPaying] = useState<Boolean>(false);
  const [isIndicating, setIsIndicating] = useState<Boolean>(false);
  const dialogRef = useRef<HTMLDialogElement>(null);

  useEffect(() => {
    const timerInterval = setInterval(
      () =>
        setTimer((prev) => {
          if (!isPaying) {
            return prev - 1;
          }

          return prev;
        }),
      1000
    );

    return () => {
      clearInterval(timerInterval);
    };
  }, [isPaying]);

  useEffect(() => {
    if (timer <= 0) {
      setBasketList([]);
    }
  }, [timer, setBasketList]);

  useEffect(() => {
    setTimer(second);
  }, [basketList]);

  useEffect(() => {
    if (isModalOpen) {
      dialogRef.current?.showModal();
    }
  }, [isModalOpen]);

  const allCancel = () => {
    setBasketList([]);
  };

  const cancelPayment = () => {
    setIsPaying(false);
    closeModal();
  };

  const closeModal = () => {
    dialogRef.current?.close();
    setIsModalOpen(false);
  };

  const openModal = () => {
    setIsModalOpen(true);
    setIsPaying(true);
  };

  return (
    <div className={styles.basket}>
      <div className={styles.basketList}>
        <div className={styles.list}>
          {basketList.map((item) => (
            <BasketItem
              key={`${item.id}_${Object.values(item.options).join("_")}`}
              item={item}
              setBasketList={setBasketList}
            />
          ))}
        </div>
        <div className={styles.total}>
          <div className={styles.total_title}>총 수량</div>
          <div className={styles.total_count}>
            {basketList.reduce((a, b) => {
              return a + b.count;
            }, 0)}
          </div>
          <div className={styles.total_price}>
            {basketList.reduce((a, b) => {
              return a + b.count * b.price;
            }, 0)}{" "}
            원
          </div>
        </div>
      </div>
      <div className={styles.basketButtons}>
        <div>
          <div>
            <div className={styles.timerTitle}>남은 시간</div>
            <div className={styles.timerWrapper}>
              <p className={styles.timer}>{timer}</p>
              <p>&nbsp; 초</p>
            </div>
          </div>
          <button className={styles.allCancelButton} onClick={allCancel}>
            전체취소
          </button>
        </div>

        <button className={styles.paymentButton} onClick={openModal}>
          결제하기
        </button>
      </div>
      {isModalOpen && (
        <PaymentModal
          dialogRef={dialogRef}
          closeModal={closeModal}
          cancelPayment={cancelPayment}
          setIsIndicating={setIsIndicating}
          basketList={basketList}
          setReceiptData={setReceiptData}
        />
      )}
      {isIndicating && <Indicator />}
    </div>
  );
};

const BasketItem = ({
  item,
  setBasketList,
}: {
  item: basketList;
  setBasketList: React.Dispatch<React.SetStateAction<any[]>>;
}) => {
  const [menuCount, setMenuCount] = useState(item.count);

  const increase = () => {
    if (menuCount + 1 < 99) {
      setMenuCount(menuCount + 1);
    }
  };

  const decrease = () => {
    if (menuCount - 1 <= 0) {
      setBasketList((prev) => {
        return prev.filter(
          (i) =>
            i.id !== item.id ||
            JSON.stringify(i.options) !== JSON.stringify(item.options)
        );
      });
    } else {
      setMenuCount(menuCount - 1);
    }
  };

  useEffect(() => {
    setBasketList((prev) => {
      const itemIndex = prev.findIndex(
        (i) =>
          i.id === item.id &&
          JSON.stringify(i.options) === JSON.stringify(item.options)
      );

      if (itemIndex !== -1) {
        const newBasketList = [...prev];
        newBasketList[itemIndex] = {
          ...newBasketList[itemIndex],
          count: menuCount,
        };
        return newBasketList;
      }

      return prev;
    });
  }, [menuCount, item.id, item.options, setBasketList]);

  useEffect(() => {
    setMenuCount(item.count);
  }, [item.count]);

  const totalPrice = menuCount * item.price;

  return (
    <div className={styles.basketItems}>
      <div className={styles.item}>
        <div className={styles.itemName}>
          {item.name}
          <div className={styles.options}>
            {Object.entries(item.options)
              .map(([_, { name }]) => name)
              .join(" / ")}
          </div>
        </div>
        <div className={styles.count}>
          <div className={styles.minusButton} onClick={decrease}></div>
          <div className={styles.countNumber}>{menuCount}</div>
          <div className={styles.plusButton} onClick={increase}></div>
        </div>
        <div className={styles.price}>{`${totalPrice} 원`}</div>
      </div>
    </div>
  );
};
