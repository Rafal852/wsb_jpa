insert into address (id, address_line1, address_line2, city, postal_code)
values (901, 'ul. Kiełczowska', '22/4', 'Wrocław', '51-356'),
       (902, 'ul. Sycowska', '19', 'Wrocław', '51-319'),
       (903, 'ul. Kłokoczycka', '1', 'Wrocław', '51-321'),
       (904, 'ul. Pilczycka', '15/321', 'Wrocław', '51-300');

insert into doctor (address_id, id, doctor_number, email, first_name, last_name, telephone_number, specialization)
values
    (901, 1, 'DOC001', 'jan.kowalski@onet.com', 'Jan', 'Kowalski', '123456789', 'GP'),
    (902, 2, 'DOC002', 'anna.nowak@gmail.com', 'Anna', 'Nowak', '987644321', 'DERMATOLOGIST'),
    (903, 3, 'DOC003', 'piotr.wisniewski@wp.com', 'Piotr', 'Wiśniewski', '458349123', 'SURGEON');

insert into Patient (date_of_birth, address_id, id, email, first_name, last_name, patient_number, telephone_number)
values
    ('1990-05-15', 901, 1, 'anna.nowak@example.com', 'Anna', 'Nowak', 'PAT001', '987654321'),
    ('1995-07-21', 902, 2, 'jozek.kolano@example.com', 'Józek', 'Kolano', 'PAT002', '999999999'),
    ('2000-06-01', 903, 3, 'maciek.reka@example.com', 'Maciek', 'Ręka', 'PAT003', '111111111'),
    ('1975-05-12', 904, 4, 'aldona.mila@example.com', 'Aldona', 'Miła', 'PAT004', '222222222');

insert into MEDICAL_TREATMENT (id, description, type)
values
    (1, 'Badanie krwi - morfologia', 'EKG'),
    (2, 'Rezonans magnetyczny głowy', 'RTG'),
    (3, 'Usunięcie znamienia skórnego', 'USG');


insert into visit (doctor_id, id, medical_treatment_id, patient_id, time, description)
values
    (1, 1, 3, 1, '2025-03-20 10:00:00', 'Kontrola po operacji.'),
    (2, 2, 2, 1, '2025-03-21 14:00:00', 'Diagnostyka bólu głowy.'),
    (3, 3, 1, 1, '2025-03-22 09:30:00', 'Badanie okresowe - morfologia.');