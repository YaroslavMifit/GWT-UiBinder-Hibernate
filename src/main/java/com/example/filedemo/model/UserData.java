package com.example.filedemo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@Entity
// Файл 092018B1.xlsx
@Table(name = "user_data")
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    // Файл 092018N1
    @ManyToOne
    @JoinColumn (name="creditOrganization_id")
    private CreditOrganization creditOrganization;

    // Файл NAMES
    @ManyToOne
    @JoinColumn (name="score_id")
    private Score score;

    //Входящие остатки «в рублях», тыс. руб
    @Column(name = "incoming_balances_in_rubles")
    private String incomingBalancesInRubles;

    //Входящие остатки «ин. вал., драг. металлы», тыс. руб
    @Column(name = "incoming_balances_drag_metals")
    private String incomingBalancesDragMetals;

    //Входящие остатки «итого», тыс. руб.; счета Депо – в штуках
    @Column(name = "incoming_balances_of_total")
    private String incomingBalancesOfTotal;

    //Обороты за отчетный период по дебету (активу) «в рублях», тыс. руб
    @Column(name = "turnovers_debit_in_rubles")
    private String turnoversDebitInRubles;

    //Обороты за отчетный период по дебету (активу) «ин. вал., драг. металлы», тыс. руб
    @Column(name = "turnovers_debit_drag_metals")
    private String turnoversDebitDragMetals;

    //Обороты за отчетный период по дебету (активу) «итого», тыс. руб.; счета Депо – в штуках
    @Column(name = "turnovers_debit_of_total")
    private String turnoversDebitOfTotal;

    //Обороты за отчетный период по кредиту (пассиву) «в рублях», тыс. руб.
    @Column(name = "turnovers_credit_in_rubles")
    private String turnoversCreditInRubles;

    //Обороты за отчетный период по кредиту (пассиву) «ин. вал., драг. металлы», тыс. руб.
    @Column(name = "turnovers_credit_drag_metals")
    private String turnoversCreditDragMetals;

    //Обороты за отчетный период по кредиту (пассиву) «итого», тыс. руб.;
    @Column(name = "turnovers_credit_of_total")
    private String turnoversCreditOfTotal;

    //Исходящие остатки «в рублях», тыс. руб.
    @Column(name = "outgoing_balances_in_rubles")
    private String outgoingBalancesInRubles;

    //Исходящие остатки «ин. вал., драг. металлы», тыс. руб.
    @Column(name = "outgoing_balances_drag_metals")
    private String outgoingBalancesDragMetals;

    //Исходящие остатки «итого», тыс. руб.;
    @Column(name = "outgoing_balances_of_total")
    private String outgoingBalancesOfTotal;

}
