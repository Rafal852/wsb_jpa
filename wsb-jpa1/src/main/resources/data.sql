insert into address (id, address_line1, address_line2, city, postal_code)
            values (901, 'xx', 'yy', 'city', '60-400'),
                   (903, 'ul. Rynek 5', NULL, 'Kraków', '30-001'),
                   (904, 'ul. Spacerowa 12', 'lokal 5', 'Gdańsk', '80-300');

INSERT INTO doctor (address_id, id, doctor_number, email, first_name, last_name, telephone_number, specialization)
            VALUES
                    (901, 1, 'DOC001', 'jan.kowalski@onet.com', 'Jan', 'Kowalski', '123456789', 'GP'),
                    (903, 2, 'DOC002', 'anna.nowak@gmail.com', 'Anna', 'Nowak', '987644321', 'DERMATOLOGIST'),
                    (904, 3, 'DOC003', 'piotr.wisniewski@wp.com', 'Piotr', 'Wiśniewski', '458349123', 'SURGEON');

INSERT INTO Patient (date_of_birth, address_id, id, email, first_name, last_name, patient_number, telephone_number)
            VALUES
                    ('1990-05-15', 901, 1, 'anna.nowak@example.com', 'Anna', 'Nowak', 'PAT001', '987654321');

INSERT INTO MEDICAL_TREATMENT (id, description, type)
            VALUES
                    (1, 'Badanie krwi - morfologia', 'EKG'),
                    (2, 'Rezonans magnetyczny głowy', 'RTG'),
                    (3, 'Usunięcie znamienia skórnego', 'USG');


INSERT INTO visit (doctor_id, id, medical_treatment_id, patient_id, time, description)
            VALUES
                    (1, 1, 3, 1, '2025-03-20 10:00:00', 'Kontrola po operacji.'),
                    (2, 2, 2, 1, '2025-03-21 14:00:00', 'Diagnostyka bólu głowy.'),
                    (3, 3, 1, 1, '2025-03-22 09:30:00', 'Badanie okresowe - morfologia.');