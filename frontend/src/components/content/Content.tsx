import { useEffect, useRef, useState } from "react";
import styles from "../../style/Content.module.css";
import { MenuListType, Product, TabMockDataType } from "../../utils/types";
import { MenuItem, menuDetails, menuList } from "../../mockData";
import { SelectModal } from "./SelectModal";

interface ContentProps {
  setBasketList: React.Dispatch<React.SetStateAction<any[]>>;
  selectedTab: TabMockDataType | "";
}

export const Content = ({ setBasketList, selectedTab }: ContentProps) => {
  const mockData: MenuListType = menuList;
  const [currentData, setCurrentData] = useState<Product[]>([]);

  useEffect(() => {
    if (selectedTab === "") {
      return;
    }

    setCurrentData(mockData[selectedTab]);
  }, [selectedTab, mockData]);

  return (
    <div className={styles.content}>
      {currentData.length === 0 ? (
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
      alt="이미지"
    />
  );
};

const MenuList = ({
  currentData,
  setBasketList,
}: {
  currentData: Product[];
  setBasketList: React.Dispatch<React.SetStateAction<any[]>>;
}) => {
  const detailMockData = menuDetails;

  const [isModalOpen, setIsModalOpen] = useState<Boolean>(false);
  const [modalData, setModalData] = useState<MenuItem | {}>({});
  const dialogRef = useRef<HTMLDialogElement>(null);

  const handleMenuClick = (item: Product) => {
    setIsModalOpen(!isModalOpen);
    setModalData(detailMockData[item.id]);
  };

  useEffect(() => {
    if (isModalOpen) {
      dialogRef.current?.showModal();
    }
  }, [isModalOpen]);

  return (
    <div className={styles.menuList}>
      {currentData.map((item, index) => (
        <Menu key={index} item={item} handler={handleMenuClick} />
      ))}

      {isModalOpen ? (
        <SelectModal
          modalData={modalData as MenuItem}
          setBasketList={setBasketList}
          setIsModalOpen={setIsModalOpen}
          dialogRef={dialogRef}
        />
      ) : (
        ""
      )}
    </div>
  );
};

const Menu = ({
  handler,
  item,
}: {
  handler: (item: Product) => void;
  item: Product;
}) => {
  return (
    <div className={styles.menu} key={item.name} onClick={() => handler(item)}>
      {item.isSignature ? <div className={styles.signature}>인기</div> : ""}
      <img
        src="https://media.basecamp.team/media/travelagent/99/imagecontent/%EC%8A%A4%ED%83%80%EB%B2%85%EC%8A%A4_%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png"
        alt="이미지"
      />
      <div className={styles.textDiv}>{`${item.name}`}</div>
      <div className={styles.textDiv}>{`${item.price}원`}</div>
    </div>
  );
};
