import {Catalog} from "./Catalog"
import { Product } from "./Product"

export interface CartModelServer {
    total: number,
    data: [{
        product: Catalog,
        numInCart: number
    }]
}

export interface CartModelPublic {
    total: number,
    prodData: [
        {
            id: number,
            incart: number
        }
    ]
}

export interface CartModel {
    products: [{
        product: Product,
        inCart: number
    }];
    //Product[];
    total: number;
}