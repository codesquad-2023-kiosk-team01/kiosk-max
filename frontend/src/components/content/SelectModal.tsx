import React, { useEffect, useState } from "react";
import { MenuItem } from "../../mockData";
import styles from "../../style/Content.module.css";

type SelectModalType = {
  modalData: MenuItem;
  setIsModalOpen: (value: Boolean) => void;
  setBasketList: React.Dispatch<React.SetStateAction<any[]>>;
  dialogRef: React.RefObject<HTMLDialogElement>;
};

export const SelectModal = ({
  modalData,
  setIsModalOpen,
  setBasketList,
  dialogRef,
}: SelectModalType) => {
  const [menuCount, setMenuCount] = useState(1);
  const [selectedOptions, setSelectedOptions] = useState<
    Record<string, string>
  >({});
  const [isAllSelected, setIsAllSelected] = useState<boolean>(false);

  useEffect(() => {
    const allSelected = Object.keys(modalData.options).every(
      (optionKey) => selectedOptions[optionKey]
    );

    setIsAllSelected(allSelected && menuCount > 0);
  }, [selectedOptions, menuCount, modalData.options]);

  const increase = () => {
    if (menuCount + 1 < 99) {
      setMenuCount(menuCount + 1);
    }
  };

  const decrease = () => {
    if (menuCount - 1 > 0) {
      setMenuCount(menuCount - 1);
    }
  };

  const handleOptionChange = (optionKey: string, optionValue: string) => {
    setSelectedOptions({
      ...selectedOptions,
      [optionKey]: optionValue,
    });
  };

  const handleAddButton = () => {
    const obj = {
      id: modalData.id,
      name: modalData.name,
      price: modalData.price,
      count: menuCount,
      image: modalData.image,
      options: selectedOptions,
    };

    setBasketList((prev) => {
      const itemIndex = prev.findIndex(
        (item) =>
          item.id === obj.id &&
          JSON.stringify(item.options) === JSON.stringify(obj.options)
      );

      if (itemIndex > -1) {
        const newBasketList = [...prev];
        const existingItem = { ...newBasketList[itemIndex] };
        existingItem.count += obj.count;
        newBasketList[itemIndex] = existingItem;
        return newBasketList;
      }

      return [...prev, obj];
    });

    setIsModalOpen(false);
  };

  return (
    <dialog
      ref={dialogRef}
      className={styles.modal}
      onClose={() => setIsModalOpen(false)}
    >
      <button
        className={styles.closeButton}
        onClick={() => {
          dialogRef.current?.close();
          setIsModalOpen(false);
          setMenuCount(1);
        }}
      >
        <div className={styles.closeLogo}></div>
      </button>
      <div className={styles.modalContent}>
        <div className={styles.modalMenu} key={modalData.name}>
          {modalData.isSignature ? (
            <div className={styles.signature}>인기</div>
          ) : (
            ""
          )}
          <img
            src="https://media.basecamp.team/media/travelagent/99/imagecontent/%EC%8A%A4%ED%83%80%EB%B2%85%EC%8A%A4_%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png"
            alt="이미지"
          />
          <div className={styles.ModalTextDiv}>{`${modalData.name}`}</div>
          <div className={styles.ModalTextDiv}>{`${modalData.price}원`}</div>
        </div>
        <div className={styles.menuOptions}>
          {Object.keys(modalData.options).map((optionKey, index1) => (
            <div key={optionKey + index1} className={styles.option}>
              {modalData.options[optionKey].map((optionValue, index2) => (
                <React.Fragment key={optionKey + optionValue + index2}>
                  <input
                    type="radio"
                    className={styles.optionRadio}
                    id={optionValue}
                    name={optionKey}
                    value={optionValue}
                    onChange={() => handleOptionChange(optionKey, optionValue)}
                  />
                  <label htmlFor={optionValue} className={styles.optionButton}>
                    {optionValue}
                  </label>
                </React.Fragment>
              ))}
            </div>
          ))}

          <div className={styles.counter}>
            <button onClick={decrease}>-</button>
            <div className={styles.countNumber}>{menuCount}</div>
            <button onClick={increase}>+</button>
          </div>
        </div>
      </div>
      <button
        className={styles.addButton}
        disabled={!isAllSelected}
        onClick={isAllSelected ? handleAddButton : undefined}
      >
        담기
      </button>
    </dialog>
  );
};
