export interface Shop {
  idBoutique: number;
  name: string;
  address: {
    roadNumber: string;
    postCode: string;
    city: string;
  };
  coverPicture: string;
  description: string;
  email: string;
  boss: {
    idUser: number;
    firstName: string;
    surname: string;
    phoneNumber: string;
    address: {
      roadNumber: string;
      postCode: string;
      city: string;
    };
    email: string;
  };
  orders: any[];
  products: any[];
}
