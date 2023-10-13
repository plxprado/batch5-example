DROP TABLE IF EXISTS public.person;

CREATE TABLE public.person (
	id int4 NOT NULL,
	"name" varchar NOT NULL,
	age int4 NOT NULL,
	financial_situation varchar NOT NULL,
	paid_installment bool NOT NULL,
	CONSTRAINT person_pk PRIMARY KEY (id)
);
CREATE UNIQUE INDEX check_duplicity_person_situation_pk ON public.person USING btree (name, age, financial_situation, paid_installment);

-- Permissions

ALTER TABLE public.person OWNER TO postgres;
GRANT ALL ON TABLE public.person TO postgres;