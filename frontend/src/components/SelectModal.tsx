import React, { useEffect, useRef, useState } from "react";
import styles from "../style/SelectModal.module.css";
import { MenuItem } from "./Content";

type MenuOptionProps = {
  optionKey: string;
  optionValue: { id: number; name: string };
  handleOptionChange: (
    optionKey: string,
    optionValue: { id: number; name: string }
  ) => void;
};

type SelectModalProps = {
  modalData: MenuItem;
  setIsModalOpen: (value: Boolean) => void;
  setBasketList: React.Dispatch<React.SetStateAction<any[]>>;
};

export const SelectModal = ({
  modalData,
  setIsModalOpen,
  setBasketList,
}: SelectModalProps) => {
  const [menuCount, setMenuCount] = useState(1);
  const [selectedOptions, setSelectedOptions] = useState<
    Record<string, { id: number; name: string }>
  >({});
  const [isAllSelected, setIsAllSelected] = useState<boolean>(false);
  const dialogRef = useRef<HTMLDialogElement>(null);

  useEffect(() => {
    dialogRef.current?.showModal();
  }, []);

  useEffect(() => {
    const allSelected = Object.keys(modalData.options).every(
      (optionKey) => selectedOptions[optionKey]
    );

    setIsAllSelected(allSelected && menuCount > 0);
  }, [modalData.options, selectedOptions, menuCount]);

  const increase = () => {
    setMenuCount((prev) => {
      if (prev + 1 < 99) {
        return prev + 1;
      }
      return prev;
    });
  };

  const decrease = () => {
    setMenuCount((prev) => {
      if (prev > 1) {
        return prev - 1;
      }
      return prev;
    });
  };

  const handleOptionChange = (
    optionKey: string,
    optionValue: { id: number; name: string }
  ) => {
    setSelectedOptions({
      ...selectedOptions,
      [optionKey]: optionValue,
    });
  };

  const closeModalButton = () => {
    dialogRef.current?.close();
    setIsModalOpen(false);
    setMenuCount(1);
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
      <button className={styles.closeButton} onClick={closeModalButton}>
        <div className={styles.closeLogo}></div>
      </button>
      <div
        className={`${styles.modalContent} ${
          Object.keys(modalData.options).length === 0 && styles.noneOptions
        }`}
      >
        <div className={styles.modalMenu} key={modalData.name}>
          <img src={modalData.image} alt={modalData.name} />
          <div className={styles.ModalTextDiv}>{`${modalData.name}`}</div>
          <div className={styles.ModalTextDiv}>{`${modalData.price}원`}</div>
        </div>
        <div className={styles.menuOptions}>
          {Object.keys(modalData.options).map((optionKey) => (
            <div key={optionKey} className={styles.option}>
              {modalData.options[optionKey].map((optionValue) => (
                <MenuOption
                  key={optionValue.id}
                  optionKey={optionKey}
                  optionValue={optionValue}
                  handleOptionChange={handleOptionChange}
                />
              ))}
            </div>
          ))}
          <div className={styles.counter}>
            <div className={styles.minusButton} onClick={decrease}></div>
            <div className={styles.countNumber}>{menuCount}</div>
            <div className={styles.plusButton} onClick={increase}></div>
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

const MenuOption = ({
  optionKey,
  optionValue,
  handleOptionChange,
}: MenuOptionProps) => {
  return (
    <React.Fragment>
      <input
        type="radio"
        className={styles.optionRadio}
        id={optionValue.id.toString()}
        name={optionKey}
        value={optionValue.name}
        onChange={() => handleOptionChange(optionKey, optionValue)}
      />
      <label
        htmlFor={optionValue.id.toString()}
        className={styles.optionButton}
      >
        {optionValue.name}
      </label>
    </React.Fragment>
  );
};
