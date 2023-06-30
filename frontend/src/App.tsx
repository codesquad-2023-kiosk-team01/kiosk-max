import { useEffect, useState } from "react";
import { Navigation } from "./components/Navigation";
import styles from "./style/App.module.css";
import { Content } from "./components/Content";
import { Basket } from "./components/Basket";
import { Receipt } from "./components/Receipt";

export type BasketList = {
  count: number;
  id: number;
  image: string;
  name: string;
  options: {
    [key: string]: string;
  };
  price: number;
};

type Option = {
  id: number;
  name: string;
};

type OptionObject = { [key: string]: Option | undefined };

type Item = {
  name: string;
  quantity: number;
  price: number;
  options: OptionObject[];
};

export type ReceiptData = {
  id: number;
  items: Item[];
  payments: string;
  amount: number;
  total: number;
  remain: number;
};

export const App = () => {
  const [categoryId, setCategoryID] = useState<number>(0);
  const [basketList, setBasketList] = useState<any[]>([]);
  const [currentPage, setCurrentPage] = useState(window.location.pathname);
  const [receiptData, setReceiptData] = useState<ReceiptData>();

  const handleTabClick = (id: number) => {
    if (id !== categoryId) {
      setCategoryID(id);
    }
  };

  const clearState = () => {
    setCategoryID(0);
    setBasketList([]);
    setCurrentPage("/");
    setReceiptData(undefined);
  };

  useEffect(() => {
    window.history.pushState(null, "", currentPage);
  }, [currentPage]);

  useEffect(() => {
    if (receiptData !== undefined) {
      setCurrentPage("/receipt");
    }
  }, [receiptData]);

  const router = () => {
    const main = (
      <>
        <Navigation categoryId={categoryId} handleTabClick={handleTabClick} />
        <Content categoryId={categoryId} setBasketList={setBasketList} />
        {basketList.length > 0 && (
          <Basket
            basketList={basketList}
            setBasketList={setBasketList}
            setReceiptData={setReceiptData}
          />
        )}
      </>
    );

    switch (currentPage) {
      case "/":
        return main;
      case "/receipt":
        if (receiptData) {
          return <Receipt receiptData={receiptData} clearState={clearState} />;
        }
        setCurrentPage("/");

        return main;
      default:
        setCurrentPage("/");
    }
  };
  return <div className={styles["app"]}>{router()}</div>;
};
