--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

-- Started on 2024-05-07 22:14:40 CEST

SET
statement_timeout = 0;
SET
lock_timeout = 0;
SET
idle_in_transaction_session_timeout = 0;
SET
client_encoding = 'UTF8';
SET
standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET
check_function_bodies = false;
SET
xmloption = content;
SET
client_min_messages = warning;
SET
row_security = off;

SET
default_tablespace = '';

SET
default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 83051)
-- Name: accountant; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.accountant
(
    factory_id bigint,
    id         bigint NOT NULL,
    address    character varying(255),
    name       character varying(255),
    phone      character varying(255)
);


ALTER TABLE public.accountant OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 83050)
-- Name: accountant_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.accountant_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER SEQUENCE public.accountant_id_seq OWNER TO postgres;

--
-- TOC entry 3709 (class 0 OID 0)
-- Dependencies: 216
-- Name: accountant_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.accountant_id_seq OWNED BY public.accountant.id;


--
-- TOC entry 219 (class 1259 OID 83060)
-- Name: cart; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cart
(
    id      bigint NOT NULL,
    address character varying(255),
    email   character varying(255),
    name    character varying(255),
    phone   character varying(255)
);


ALTER TABLE public.cart OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 83059)
-- Name: cart_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cart_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER SEQUENCE public.cart_id_seq OWNER TO postgres;

--
-- TOC entry 3710 (class 0 OID 0)
-- Dependencies: 218
-- Name: cart_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.cart_id_seq OWNED BY public.cart.id;


--
-- TOC entry 220 (class 1259 OID 83068)
-- Name: cart_products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cart_products
(
    cart_id     bigint NOT NULL,
    products_id bigint NOT NULL
);


ALTER TABLE public.cart_products OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 83072)
-- Name: category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.category
(
    id    bigint NOT NULL,
    image character varying(255),
    name  character varying(255),
    type  character varying(255)
);


ALTER TABLE public.category OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 83071)
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER SEQUENCE public.category_id_seq OWNER TO postgres;

--
-- TOC entry 3711 (class 0 OID 0)
-- Dependencies: 221
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.category_id_seq OWNED BY public.category.id;


--
-- TOC entry 224 (class 1259 OID 83081)
-- Name: factory; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.factory
(
    id      bigint NOT NULL,
    address character varying(255),
    name    character varying(255)
);


ALTER TABLE public.factory OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 83080)
-- Name: factory_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.factory_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER SEQUENCE public.factory_id_seq OWNER TO postgres;

--
-- TOC entry 3712 (class 0 OID 0)
-- Dependencies: 223
-- Name: factory_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.factory_id_seq OWNED BY public.factory.id;


--
-- TOC entry 226 (class 1259 OID 83090)
-- Name: order; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."order"
(
    order_amount  numeric(38, 2),
    order_status  integer DEFAULT 0,
    create_time   timestamp(6) without time zone,
    id            bigint NOT NULL,
    update_time   timestamp(6) without time zone,
    user_id       bigint,
    buyer_address character varying(255),
    buyer_email   character varying(255),
    buyer_name    character varying(255),
    buyer_phone   character varying(255),
    description   character varying(255),
    name          character varying(255)
);


ALTER TABLE public."order" OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 83099)
-- Name: order-products_product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."order-products_product"
(
    order_product_id bigint NOT NULL,
    product_id       bigint NOT NULL
);


ALTER TABLE public."order-products_product" OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 83089)
-- Name: order_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.order_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER SEQUENCE public.order_id_seq OWNER TO postgres;

--
-- TOC entry 3713 (class 0 OID 0)
-- Dependencies: 225
-- Name: order_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.order_id_seq OWNED BY public."order".id;


--
-- TOC entry 215 (class 1259 OID 82804)
-- Name: order_product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.order_product
(
    order_product_id bigint NOT NULL,
    product_id       bigint NOT NULL
);


ALTER TABLE public.order_product OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 83103)
-- Name: order_products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.order_products
(
    quantity integer NOT NULL,
    cart_id  bigint,
    id       bigint  NOT NULL,
    order_id bigint
);


ALTER TABLE public.order_products OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 83102)
-- Name: order_products_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.order_products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER SEQUENCE public.order_products_id_seq OWNER TO postgres;

--
-- TOC entry 3714 (class 0 OID 0)
-- Dependencies: 228
-- Name: order_products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.order_products_id_seq OWNED BY public.order_products.id;


--
-- TOC entry 231 (class 1259 OID 83110)
-- Name: products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.products
(
    max_quantity   integer          NOT NULL,
    price          double precision NOT NULL,
    quantity       integer          NOT NULL,
    category_id    bigint,
    id             bigint           NOT NULL,
    subcategory_id bigint,
    brand          character varying(255),
    colour         character varying(255),
    description    character varying(3000),
    image          character varying(255),
    name           character varying(255),
    image2         character varying(255),
    image3         character varying(255),
    image4         character varying(255)
);


ALTER TABLE public.products OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 83109)
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER SEQUENCE public.products_id_seq OWNER TO postgres;

--
-- TOC entry 3715 (class 0 OID 0)
-- Dependencies: 230
-- Name: products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;


--
-- TOC entry 233 (class 1259 OID 83119)
-- Name: subcategory; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.subcategory
(
    category_id bigint,
    id          bigint NOT NULL,
    image       character varying(255),
    name        character varying(255),
    type        character varying(255)
);


ALTER TABLE public.subcategory OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 83118)
-- Name: subcategory_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.subcategory_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER SEQUENCE public.subcategory_id_seq OWNER TO postgres;

--
-- TOC entry 3716 (class 0 OID 0)
-- Dependencies: 232
-- Name: subcategory_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.subcategory_id_seq OWNED BY public.subcategory.id;


--
-- TOC entry 235 (class 1259 OID 83128)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users
(
    cart_id    bigint,
    factory_id bigint,
    id         bigint NOT NULL,
    address    character varying(255),
    email      character varying(255),
    name       character varying(255),
    password   character varying(255),
    phone      character varying(255),
    role       character varying(255)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 83127)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- TOC entry 3717 (class 0 OID 0)
-- Dependencies: 234
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 3495 (class 2604 OID 83054)
-- Name: accountant id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accountant ALTER COLUMN id SET DEFAULT nextval('public.accountant_id_seq'::regclass);


--
-- TOC entry 3496 (class 2604 OID 83063)
-- Name: cart id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart ALTER COLUMN id SET DEFAULT nextval('public.cart_id_seq'::regclass);


--
-- TOC entry 3497 (class 2604 OID 83075)
-- Name: category id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category ALTER COLUMN id SET DEFAULT nextval('public.category_id_seq'::regclass);


--
-- TOC entry 3498 (class 2604 OID 83084)
-- Name: factory id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.factory ALTER COLUMN id SET DEFAULT nextval('public.factory_id_seq'::regclass);


--
-- TOC entry 3500 (class 2604 OID 83094)
-- Name: order id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."order" ALTER COLUMN id SET DEFAULT nextval('public.order_id_seq'::regclass);


--
-- TOC entry 3501 (class 2604 OID 83106)
-- Name: order_products id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_products ALTER COLUMN id SET DEFAULT nextval('public.order_products_id_seq'::regclass);


--
-- TOC entry 3502 (class 2604 OID 83113)
-- Name: products id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);


--
-- TOC entry 3503 (class 2604 OID 83122)
-- Name: subcategory id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subcategory ALTER COLUMN id SET DEFAULT nextval('public.subcategory_id_seq'::regclass);


--
-- TOC entry 3504 (class 2604 OID 83131)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 3685 (class 0 OID 83051)
-- Dependencies: 217
-- Data for Name: accountant; Type: TABLE DATA; Schema: public; Owner: postgres
--

-- Inserting data into the public.accountant table
-- Inserting data into the public.accountant table
INSERT INTO public.accountant (factory_id, id, address, name, phone)
VALUES (1, 1, 'address_value_1', 'name_value_1', 'phone_value_1'),
       (2, 2, 'address_value_2', 'name_value_2', 'phone_value_2'),
       (3, 3, 'address_value_3', 'name_value_3', 'phone_value_3');

-- Inserting data into the public.cart table
-- Inserting data into the public.cart table
INSERT INTO public.cart (id, address, email, name, phone)
VALUES (1, 'Onderwijslaan 51- 2', 'arman.setoian@gmail.com', 'Arman Setoian', '0491283791'),
       (2, 'Some other address', 'another.email@example.com', 'Another Name', '9876543210');
-- Repeat the above INSERT statement for each row in the public.cart table
-- Repeat the above INSERT statement for each row in the public.cart table
;
-- Similarly, create INSERT statements for other tables like public.cart_products, public.category, public.factory, public.order, public.order-products_product, public.order_product, public.order_products, and public.products
INSERT INTO public.factory (id, name, address)
VALUES (2, 'Factory Name', 'Factory Address');

INSERT INTO public.factory (id, name, address)
VALUES (1, 'Factory', 'Address');
INSERT INTO public.factory (id, name, address)
VALUES (3, 'Name', 'Factory');

-- Добавление записей в таблицу category
INSERT INTO public.category (id, name, type, image)
VALUES (1111, 'Clothes', 'Clothes',
        'https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcSOHOST_SBgbS8RIFiU_VuNhV8xozVMQAHyHI_CzbMDrLihAa_WoBXp1Dsd9Qaf6-XOXBzt6qA18e2VFKr3uz0eW8uYST0Pna55ab9qeXMggK8qzgonTJip'),
       (2222, 'Shoes', 'Shoes', 'https://thehoffbrand.com/cdn/shop/files/12202018_6.jpg?v=1713869906&width=1000'),
       (3333, 'Accessories', 'Accessories',
        'https://www.realmenrealstyle.com/wp-content/uploads/2023/08/Over-Accessorizing.jpg'),
       (4444, 'Beauty ', 'Beauty',
        'https://www.gloskinbeauty.com/media/catalog/tmp/category/face-square-cat-image.jpg'),
       (5555, 'Electronic Devices', 'Electronic Devices',
        'https://t3.ftcdn.net/jpg/03/66/66/64/240_F_366666486_wT4G7S7S8mZbyTuk4GiJgvqSXscCfjNR.jpg'),
       (6666, 'Suits', 'Business Suits',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/q/o/1701708314_933C701A5884_C585_E01_GHC.jpg?imwidth=3000');

-- Добавление записей в таблицу subcategory
INSERT INTO public.subcategory (id, name, type, image, category_id)
VALUES (1111, 'T-Shirts', 'Premium T-shirts',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/z/b/1700682469_H_24_1_LOOK_031_E07_GHC.jpg?imwidth=3000',
        1111),
       (2222, 'Shoes', 'Premium Shoes',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/L/e/1714988841_H_24_2_LOOK_027_E23_GHC.jpg?imwidth=3000',
        2222),
       (3333, 'Watches', 'Classic Watches',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/u/z/1677155000_CD04111X1248_0000_E01_GHC.jpg?imwidth=3000',
        3333),
       (4444, 'Perfume', 'Premium Perfume',
        'https://www.dior.com/dw/image/v2/BGXS_PRD/on/demandware.static/-/Sites-master_dior/default/dw64348667/Y0997096/Y0997096_C099700474_E02_GHC.jpg?sw=800',
        4444),
       (5555, 'Headsets', 'Headset',
        'https://static.bax-shop.es/image/product/834746/4413847/ecc0a0e4/1687242378PIC_DT700PRO-X_21-04_perspective-white_v1.jpg',
        5555),
       (6666, 'Classic', 'Classic Costume',
        'https://media.brunellocucinelli.com/image/upload/f_auto,q_auto/c_scale,w_800/t_bc_sfcc_v_image/v1705058465/prod/seecommerce/original/241MG4657BPC-C003-F.jpeg?_i=AG',
        6666),
       (7777, 'Trousers', 'Premium Trousers',
        'https://www.dior.com/couture/var/dior/storage/images/folder-media/folder-productpage/folder-crossselllook/folder-homme/folder-winter-23/block-h_23_4_look_075/41367358-1-eng-GB/h_23_4_look_075.jpg?imwidth=3000',
        1111),
       (8888, 'Sport Shoes', 'Sport Shoes',
        'https://img01.ztat.net/article/spp-media-p1/203a5a7e11ca4e35a988790ec77959f1/e8d6da95135d4cd690e1a4cdf98bf63b.jpg?imwidth=1800&filter=packshot',
        2222),
       (9999, 'Bags', 'Premium Bags',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/x/l/1685612740_M0505ONGE_M030_E01_GHC.jpg?imwidth=3000',
        3333),
       (1010, 'Lipstick', 'Lipstick',
        'https://www.dior.com/dw/image/v2/BGXS_PRD/on/demandware.static/-/Library-Sites-DiorSharedLibrary/default/dwc5a2880d/images/beauty/02-MAKEUP/2024/ADDICT-2024/LP-WW/Addict24_Mood-Hero-Chrome_2400x3000.jpg?sw=1850',
        4444),
       (11111, 'Notebooks', 'Notebook', 'https://img.artencraft.be/i/3562888_949x714.jpg', 5555),
       (12122, 'Casual', 'Casual Costume',
        'https://img01.ztat.net/article/spp-media-p1/a97843a5296440c3b3210cb1e996d409/410b206f5f4743ec9e8f97cd6e221ae8.jpg?imwidth=1800',
        6666),
       (13133, 'Jeans', 'Jeans',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/K/7/1694594817_F_23_4_LOOK_702_E01_GHC.jpg?imwidth=3000',
        1111);


-- Добавление записей в таблицу products

INSERT INTO public.products (id, name, description, colour, brand, image, image2, image3, image4, price, quantity,
                             max_quantity, subcategory_id)
VALUES (11111, 'T-shirt Polo ', 'POLO
This stylish pique knitted polo feels extra soft on the inside due to the use of bamboo. This distinguishes it from cotton polo shirts. The stylish collar and tonal buttons provide the elegant look of this polo. This makes this polo shirt an indispensable item for every man.
Highlights
Light Blue Melange
Stylish ribbed collar with three-button closure
Stylish slits on the side
Longer back piece
Breathable', 'Blue', 'Polo',
        'https://bamigo.com/media/catalog/product/cache/e5003119a0e3fd304438d617b07da7a2/p/e/penton_navypeony_1.jpg',
        'https://bamigo.com/media/catalog/product/cache/e5003119a0e3fd304438d617b07da7a2/p/e/penton_navypeony_3.jpg',
        'https://bamigo.com/media/catalog/product/cache/e5003119a0e3fd304438d617b07da7a2/p/e/penton_navypeony_2.jpg',
        'https://bamigo.com/media/catalog/product/cache/e5003119a0e3fd304438d617b07da7a2/p/e/penton_navy_peony_front_1299x1299.jpg',
        100.0,
        0, 20, 1111),
       (22222, 'Dior Skatesneaker',
        'The B9S skate sneaker is inspired by the style codes of skaters and the couture look of the fashion house',
        'Light Grey', 'Dior',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/j/W/1705060698_3SN288ZZP_H868_E03_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/B/q/1705579520_H_24_2_LOOK_017_E23_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/E/d/1705579450_H_24_2_LOOK_017_E24_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/j/W/1705060699_3SN288ZZP_H868_E07_GHC.jpg?imwidth=3000',
        200.0,
        0, 30, 2222),
       (33333, 'Dior Watches',
        'Lancée en 2004, Chiffre Rouge is most impressive with its audacious audacity, with its unique identity',
        'Black', 'Dior',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/a/K/1703242053_JT_CR_LOOK_003_E01_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/U/r/1705347916_CD08352X1345_0000_E01_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/7/D/1702661669_CD08352X1345_0000_E08_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/7/D/1702661669_CD08352X1345_0000_E11_GHC.jpg?imwidth=3000',
        300.0, 0, 40, 3333),
       (44444, 'Miss Dior', 'Between strength and grace, boldness and elegance.', 'Rose', 'Dior',
        'https://www.dior.com/dw/image/v2/BGXS_PRD/on/demandware.static/-/Sites-master_dior/default/dwb75065e2/Y0997166/Y0997166_C099700897_E01_ZHC.jpg?sw=1800&sh=1200',
        'https://www.dior.com/dw/image/v2/BGXS_PRD/on/demandware.static/-/Sites-master_dior/default/dw9af9a0bf/Y0997166/Y0997166_C099700897_E02_GHC.jpg?sw=800',
        'https://www.dior.com/dw/image/v2/BGXS_PRD/on/demandware.static/-/Sites-master_dior/default/dwa414a4ab/Y0997166/Y0997166_C099700897_E04_GHC.jpg?sw=800',
        'https://www.dior.com/on/demandware.static/-/Library-Sites-DiorSharedLibrary/default/dw4a076c48/images/beauty/01-FRAGRANCES/2024/MISS-DIOR_2024/PARFUM/PDP_Y0997166/MD_Parfum_Mood_Detail-Noeud_E-Com_3700x2000.jpg',
        400.0, 0, 50, 4444),
       (55555, 'Apple AirPods Max',
        'Apple AirPods Max offers the perfect combination of high-quality audio and the effortless convenience of Airpods. The noise canceling headphones dampen all disturbing sounds from the environment. This function is perfect for busy places, such as public transport or the office.',
        'Blue', 'Apple', 'https://media.s-bol.com/mP5900yAGpLE/xQkkjP/550x724.jpg',
        'https://media.s-bol.com/nP19mmOB8qMY/xQkkjP/550x608.jpg',
        'https://media.s-bol.com/q91kppZGDEK7/xQkkjP/342x840.jpg',
        'https://media.s-bol.com/mP59G7q5MZYA/OQYYJB/550x550.jpg',
        500.0, 0, 60, 5555),
       (66666, 'Dior Costume', 'Paying tribute to this unique expertise', 'Black', 'Dior',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/v/g/1701345981_633C720A1800_C901_E01_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/v/g/1701345981_633C720A1800_C901_E03_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/v/g/1701345981_633C720A1800_C901_E05_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/v/g/1701345981_633C720A1800_C901_E07_GHC.jpg?imwidth=3000',
        600.0, 0, 70, 6666),
       (77777, 'Pantaleon large', 'Le pantalon dévoile une silhouette large et évasée', 'Brown', 'Dior',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/l/l/1712143267_447P72A3332_X1700_E01_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/l/l/1712143267_447P72A3332_X1700_E08_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/l/l/1712143267_447P72A3332_X1700_E09_GHC.jpg?imwidth=3000',
        'https://cdn.worldvectorlogo.com/logos/dior.svg',
        700.0, 0, 80, 7777),
       (88888, 'Nike Air', 'Sport shoes Nike Air', 'Black', 'Nike',
        'https://molders.be/media/catalog/product/cache/8b683330f69b8db38d5410440b5d8cfa/3/9/397_dm0829_nike_air_max_alpha_trainer_5_010_black_slash_dk_smoke_grey-129296942540-mainpicture-156648_1.jpg',
        'https://molders.be/media/catalog/product/cache/8b683330f69b8db38d5410440b5d8cfa/3/9/397_dm0829_nike_air_max_alpha_trainer_5_010_black_slash_dk_smoke_grey-129296942540-picture2-156648_2.jpg',
        'https://molders.be/media/catalog/product/cache/8b683330f69b8db38d5410440b5d8cfa/3/9/397_dm0829_nike_air_max_alpha_trainer_5_010_black_slash_dk_smoke_grey-129296942540-picture5-156648_5.jpg',
        'https://molders.be/media/catalog/product/cache/8b683330f69b8db38d5410440b5d8cfa/3/9/397_dm0829_nike_air_max_alpha_trainer_5_010_black_slash_dk_smoke_grey-129296942540-picture6-156648_6.jpg',
        800.0, 0, 90, 8888),
       (99999, 'Dior Caro-pouch',
        'De Dior Caro-pouch is een praktische en elegante creatie. Deze uitvoering met een hoofdvak dat ruim genoeg is voor al uw essentials',
        'White', 'Dior',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/l/x/1686156434_S5134UWHC_M030_E01_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/l/x/1686156434_S5134UWHC_M030_E03_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/l/x/1686156435_S5134UWHC_M030_E07_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/i/c/1687261581_F_23_3_LOOK_548_E06_GHC.jpg?imwidth=3000',
        900.0, 0, 100, 9999),
       (10101, 'Dior Lipstick', 'Dior Lipstick For woman', 'Red', 'Dior',
        'https://www.dior.com/dw/image/v2/BGXS_PRD/on/demandware.static/-/Sites-master_dior/default/dwd8033891/Y0356009/Y0356009_C035600999_E01_ZHC.jpg?sw=1800&sh=1200',
        'https://www.dior.com/dw/image/v2/BGXS_PRD/on/demandware.static/-/Sites-master_dior/default/dw223b6769/Y0356009/Y0356009_C035600999_E02_GHC.jpg?sw=800',
        'https://www.dior.com/dw/image/v2/BGXS_PRD/on/demandware.static/-/Sites-master_dior/default/dwb8af2fbc/Y0356009/Y0356009_C035600999_E04_GHC.jpg?sw=800',
        'https://www.dior.com/dw/image/v2/BGXS_PRD/on/demandware.static/-/Sites-master_dior/default/dw7341038e/Y0356009/Y0356009_C035600999_E03_GHC.jpg?sw=800',
        1000.0, 0, 110, 1010),
       (111111, 'Macbook Pro 16', 'MacbookPro 16 M3 MAX', 'Black', 'Macbook',
        'https://media.krefel.be/sys-master/products/9321010298910/1200x1200.41008607_04.webp',
        'https://media.krefel.be/sys-master/products/9321010167838/1200x1200.41008607_02.webp',
        'https://media.krefel.be/sys-master/products/9321010102302/1200x1200.41008607_01.webp',
        'https://media.krefel.be/sys-master/products/9321010429982/1200x1200.41008607_06.webp', 1100.0, 0, 120, 11111),
       (12122, 'DINICK - Colbert Costume', 'DINICK - Colbert Casual Costume', 'Green', 'DINICK - Colbert',
        'https://img01.ztat.net/article/spp-media-p1/045f08394c1547eaaaf99076d753f511/ecdb90574be84ba3bccb831b220f6f81.jpg?imwidth=1800',
        'https://img01.ztat.net/article/spp-media-p1/c69846b93442457995908f2d32587ecd/7373d24821284f5f89f8464d9ddce85d.jpg?imwidth=1800',
        'https://img01.ztat.net/article/spp-media-p1/fdbcb988fb1043d0bab0cff5f6152662/9ee47662e027466ab11c74ac0a2233dd.jpg?imwidth=1800',
        'https://img01.ztat.net/article/spp-media-p1/915423303d214573bdac42b6b69dbe8d/d2521c600d664013afff7284ce380264.jpg?imwidth=1800',
        1200.0, 0, 130, 12122),
       (13133, 'Jeans Dior', 'Jeans Dior for Woman', 'Blue', 'Dior',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/j/3/1619289989_142P30A3394_X5651_E12_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/h/P/1618248417_142P30A3394_X5651_E01_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/j/3/1619289989_142P30A3394_X5651_E13_GHC.jpg?imwidth=3000',
        'https://www.dior.com/couture/ecommerce/media/catalog/product/j/3/1619289989_142P30A3394_X5651_E14_GHC.jpg?imwidth=3000',
        1300.0, 0, 140, 13133);
--        (14, 'Product Name 14', 'Description 14', 'Colour 14', 'Brand 14', 'Image 53', 'Image 54', 'Image 55',
--         'Image 56', 1400.0, 0, 150, 2),
--        (15, 'Product Name 15', 'Description 15', 'Colour 15', 'Brand 15', 'Image 57', 'Image 58', 'Image 59',
--         'Image 60', 1500.0, 0, 160, 3),
--        (16, 'Product Name 16', 'Description 16', 'Colour 16', 'Brand 16', 'Image 61', 'Image 62', 'Image 63',
--         'Image 64', 1600.0, 0, 170, 4),
--        (17, 'Product Name 17', 'Description 17', 'Colour 17', 'Brand 17', 'Image 65', 'Image 66', 'Image 67',
--         'Image 68', 1700.0, 0, 180, 5),
--        (18, 'Product Name 18', 'Description 18', 'Colour 18', 'Brand 18', 'Image 69', 'Image 70', 'Image 71',
--         'Image 72', 1800.0, 0, 190, 6);


--
-- TOC entry 3718 (class 0 OID 0)
-- Dependencies: 216
-- Name: accountant_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.accountant_id_seq', 1, false);


--
-- TOC entry 3719 (class 0 OID 0)
-- Dependencies: 218
-- Name: cart_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cart_id_seq', 54, true);


--
-- TOC entry 3720 (class 0 OID 0)
-- Dependencies: 221
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.category_id_seq', 6, true);


--
-- TOC entry 3721 (class 0 OID 0)
-- Dependencies: 223
-- Name: factory_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.factory_id_seq', 1, false);


--
-- TOC entry 3722 (class 0 OID 0)
-- Dependencies: 225
-- Name: order_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.order_id_seq', 19, true);


--
-- TOC entry 3723 (class 0 OID 0)
-- Dependencies: 228
-- Name: order_products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.order_products_id_seq', 209, true);


--
-- TOC entry 3724 (class 0 OID 0)
-- Dependencies: 230
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.products_id_seq', 6, true);


--
-- TOC entry 3725 (class 0 OID 0)
-- Dependencies: 232
-- Name: subcategory_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.subcategory_id_seq', 7, true);


--
-- TOC entry 3726 (class 0 OID 0)
-- Dependencies: 234
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 7, true);


--
-- TOC entry 3506 (class 2606 OID 83058)
-- Name: accountant accountant_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accountant
    ADD CONSTRAINT accountant_pkey PRIMARY KEY (id);


--
-- TOC entry 3508 (class 2606 OID 83067)
-- Name: cart cart_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT cart_pkey PRIMARY KEY (id);


--
-- TOC entry 3510 (class 2606 OID 83079)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- TOC entry 3512 (class 2606 OID 83088)
-- Name: factory factory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.factory
    ADD CONSTRAINT factory_pkey PRIMARY KEY (id);


--
-- TOC entry 3514 (class 2606 OID 83098)
-- Name: order order_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."order"
    ADD CONSTRAINT order_pkey PRIMARY KEY (id);


--
-- TOC entry 3516 (class 2606 OID 83108)
-- Name: order_products order_products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_products
    ADD CONSTRAINT order_products_pkey PRIMARY KEY (id);


--
-- TOC entry 3518 (class 2606 OID 83117)
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- TOC entry 3520 (class 2606 OID 83126)
-- Name: subcategory subcategory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subcategory
    ADD CONSTRAINT subcategory_pkey PRIMARY KEY (id);


--
-- TOC entry 3522 (class 2606 OID 83137)
-- Name: users users_cart_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_cart_id_key UNIQUE (cart_id);


--
-- TOC entry 3524 (class 2606 OID 83139)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 3526 (class 2606 OID 83135)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3535 (class 2606 OID 83180)
-- Name: products fk1cf90etcu98x1e6n9aks3tel3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT fk1cf90etcu98x1e6n9aks3tel3 FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- TOC entry 3533 (class 2606 OID 83170)
-- Name: order_products fk4xn93j6lqih2g6n01j7y5htkx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_products
    ADD CONSTRAINT fk4xn93j6lqih2g6n01j7y5htkx FOREIGN KEY (cart_id) REFERENCES public.cart(id);


--
-- TOC entry 3528 (class 2606 OID 83150)
-- Name: cart_products fk7xg877l1r2f279hmlcowu1cth; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart_products
    ADD CONSTRAINT fk7xg877l1r2f279hmlcowu1cth FOREIGN KEY (products_id) REFERENCES public.products(id);


--
-- TOC entry 3527 (class 2606 OID 83140)
-- Name: accountant fkb5s61bljil1bm78y19uubasb6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accountant
    ADD CONSTRAINT fkb5s61bljil1bm78y19uubasb6 FOREIGN KEY (factory_id) REFERENCES public.factory(id);


--
-- TOC entry 3538 (class 2606 OID 83200)
-- Name: users fkcrnexpe5i83hs95bywleo3efe; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkcrnexpe5i83hs95bywleo3efe FOREIGN KEY (factory_id) REFERENCES public.factory(id);


--
-- TOC entry 3531 (class 2606 OID 83165)
-- Name: order-products_product fke10okyur6cdvldmcdci8ng6o9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."order-products_product"
    ADD CONSTRAINT fke10okyur6cdvldmcdci8ng6o9 FOREIGN KEY (order_product_id) REFERENCES public.order_products(id);


--
-- TOC entry 3537 (class 2606 OID 83190)
-- Name: subcategory fke4hdbsmrx9bs9gpj1fh4mg0ku; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subcategory
    ADD CONSTRAINT fke4hdbsmrx9bs9gpj1fh4mg0ku FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- TOC entry 3530 (class 2606 OID 83155)
-- Name: order fkh3c37jq3nrv0f2edcxk0ine72; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."order"
    ADD CONSTRAINT fkh3c37jq3nrv0f2edcxk0ine72 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3534 (class 2606 OID 83175)
-- Name: order_products fkhva1mlxebnkr41a5n7a8l1nhg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_products
    ADD CONSTRAINT fkhva1mlxebnkr41a5n7a8l1nhg FOREIGN KEY (order_id) REFERENCES public."order"(id);


--
-- TOC entry 3536 (class 2606 OID 83185)
-- Name: products fkiyysl9lg2lf7wrbs51epcocke; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT fkiyysl9lg2lf7wrbs51epcocke FOREIGN KEY (subcategory_id) REFERENCES public.subcategory(id);


--
-- TOC entry 3529 (class 2606 OID 83145)
-- Name: cart_products fknlhjc091rdu9k5c8u9xwp280w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart_products
    ADD CONSTRAINT fknlhjc091rdu9k5c8u9xwp280w FOREIGN KEY (cart_id) REFERENCES public.cart(id);


--
-- TOC entry 3539 (class 2606 OID 83195)
-- Name: users fkqmifheg6lnigfifvlmpjnuny8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkqmifheg6lnigfifvlmpjnuny8 FOREIGN KEY (cart_id) REFERENCES public.cart(id);


--
-- TOC entry 3532 (class 2606 OID 83160)
-- Name: order-products_product fkr5esgs4jdqs1e8t85xtamod3s; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."order-products_product"
    ADD CONSTRAINT fkr5esgs4jdqs1e8t85xtamod3s FOREIGN KEY (product_id) REFERENCES public.products(id);


-- Completed on 2024-05-07 22:14:40 CEST

--
-- PostgreSQL database dump complete
--

