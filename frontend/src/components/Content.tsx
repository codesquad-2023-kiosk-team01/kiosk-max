import { useEffect, useState } from "react";
import styles from "../style/Content.module.css";
import { SelectModal } from "./SelectModal";

type ContentProps = {
  setBasketList: React.Dispatch<React.SetStateAction<any[]>>;
  categoryId: number;
};

type MenuListProps = {
  currentData: menuCurrentData;
  setBasketList: React.Dispatch<React.SetStateAction<any[]>>;
};

type MenuProps = { onClick: () => void; item: MenuListType };

type MenuListType = {
  id: string;
  name: string;
  price: number;
  image: string;
  isSignature: boolean;
};

type menuCurrentData = {
  id: number;
  items: MenuListType[];
};

type OptionValueType = {
  id: number;
  name: string;
};

export type MenuItem = {
  description: string;
  id: number;
  image: string;
  name: string;
  options: { [key: string]: OptionValueType[] };
  price: number;
};

export const Content = ({ setBasketList, categoryId }: ContentProps) => {
  const [currentData, setCurrentData] = useState<menuCurrentData>();

  useEffect(() => {
    if (categoryId === 0) {
      return;
    }

    const fetchMenuList = async () => {
      const response = await fetch(
        `http://43.201.168.11:8080/api/categories/${categoryId}`
      );
      const data = await response.json();

      setCurrentData(data);
    };

    fetchMenuList();
  }, [categoryId]);

  return (
    <div className={styles.content}>
      {currentData === undefined ? (
        <Logo />
      ) : (
        <MenuList currentData={currentData} setBasketList={setBasketList} />
      )}
    </div>
  );
};

const Logo = () => {
  return (
    <img
      className={styles.logoImg}
      src="https://www.shinsegaegroupnewsroom.com/wp-content/uploads/2021/07/%EC%8A%A4%ED%83%80%EB%B2%85%EC%8A%A4_%EB%B3%B8%EB%AC%B89002-1.png"
      alt="로고 이미지"
    />
  );
};

const MenuList = ({ currentData, setBasketList }: MenuListProps) => {
  const [isModalOpen, setIsModalOpen] = useState<Boolean>(false);
  const [modalData, setModalData] = useState<MenuItem>();

  const handleMenuClick = async (item: MenuListType) => {
    const response = await fetch(
      `http://43.201.168.11:8080/api/categories/${currentData.id}/items/${item.id}`
    );
    const data = await response.json();

    setIsModalOpen(!isModalOpen);
    setModalData(data);
  };

  return (
    <div className={styles.menuList}>
      {currentData.items.map((item, index) => (
        <Menu key={index} item={item} onClick={() => handleMenuClick(item)} />
      ))}

      {isModalOpen && modalData && (
        <SelectModal
          modalData={modalData}
          setBasketList={setBasketList}
          setIsModalOpen={setIsModalOpen}
        />
      )}
    </div>
  );
};

const Menu = ({ onClick, item }: MenuProps) => {
  return (
    <div className={styles.menu} key={item.name} onClick={onClick}>
      {item.isSignature && <div className={styles.signature}>인기</div>}
      <img src={item.image} alt={item.name} />
      <div className={styles.textDiv}>{`${item.name}`}</div>
      <div className={styles.textDiv}>{`${item.price}원`}</div>
    </div>
  );
};
