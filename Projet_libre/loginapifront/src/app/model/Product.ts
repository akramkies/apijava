/* export interface Product {
    id: number;
    title: string;
    category: string;
    description: string;
    image: string,
    price: number;
    amount: number;
} */

export interface Product {
    reference: string,
    family: string,
    designation: string,
    costPrice: number,
    salePrice: number,
    amount: number,
    imgUrl: string
}

export interface Products {
    count: number;
    products: Product[];
}

